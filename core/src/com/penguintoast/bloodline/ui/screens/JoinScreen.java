package com.penguintoast.bloodline.ui.screens;

import java.net.InetAddress;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.net.Discoverer;
import com.penguintoast.bloodline.net.Discoverer.DiscoverListener;
import com.penguintoast.bloodline.net.GameClient;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.ui.skin.ButtonPadding;

public class JoinScreen extends BaseScreen {
	private List<InfoResponse> list;
	private Array<InfoResponse> servers;

	public JoinScreen() {
		servers = new Array<InfoResponse>(true, 1, InfoResponse.class);
		list = new List<InfoResponse>(Global.skin);
		list.setItems(servers);
		ScrollPane pane = new ScrollPane(list, Global.skin);
		pane.setFlickScroll(false);
		pane.setSmoothScrolling(false);

		table.add(pane).size(300, 300).colspan(3);
		pane.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (getTapCount() >= 1) {
					join();
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		table.row();

		ButtonPadding pad = Global.skin.get(ButtonPadding.class);

		TextButton joinButton = new TextButton("Join", Global.skin);
		joinButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				join();
			}
		});
		table.add(joinButton.pad(pad.top, pad.left, pad.bottom, pad.right)).space(pad.space).uniform().fill();

		TextButton refreshButton = new TextButton("Refresh", Global.skin);
		refreshButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				refreshServers();
			}
		});
		table.add(refreshButton.pad(pad.top, pad.left, pad.bottom, pad.right)).space(pad.space).uniform().fill();

		TextButton cancelButton = new TextButton("Cancel", Global.skin);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Global.game.transition(new MenuScreen());
			}
		});
		table.add(cancelButton.pad(pad.top, pad.left, pad.bottom, pad.right)).space(pad.space).uniform().fill();

	}

	private void join() {
		int index = list.getSelectedIndex();
		if (index >= 0 && servers.size > 0) {
			JoinResponse response = Network.client.joinServer(servers.get(index).address);
			if (response != null) {
				if (response.response == JoinResponse.ACCEPTED) {
					Network.host = false;
					Global.game.transition(new LobbyScreen());
				}
			} else {
				showErrorDialog();
			}
		}
	}

	private void showErrorDialog() {
		Dialog errorDialog = new Dialog("", Global.skin);
		errorDialog.setSkin(Global.skin);
		errorDialog.getContentTable().add("The server you are trying to join no longer exists.");

		Button but = new TextButton("Ok", Global.skin);
		but.pad(0, 20, 3, 20);
		errorDialog.button(but);
		errorDialog.setMovable(true);
		errorDialog.pad(10f);
		errorDialog.show(stage);
		refreshServers();
	}

	@Override
	public void show() {

		Network.client = new GameClient();
		Network.client.start();

		refreshServers();
	}

	private void refreshServers() {
		servers.clear();
		list.setItems(servers.toArray());
		Discoverer.discoverHosts(Network.UDP_PORT, 5000, Network.client.getClient().getSerialization(), new DiscoverListener() {
			@Override
			public void hostDiscovered(InetAddress addr) {
				if (addr == null) {
					return;
				}
				InfoResponse info = Network.client.queryInfo(addr);
				if (info != null && !servers.contains(info, false)) {
					info.address = addr;
					servers.add(info);
					list.setItems(servers.toArray());
				}
			}
		});
	}

}
