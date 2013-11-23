package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.widgets.BloodlineSelector;
import com.penguintoast.bloodline.gui.widgets.ChatBox;
import com.penguintoast.bloodline.gui.widgets.MapSelector;
import com.penguintoast.bloodline.gui.widgets.PlayerList;
import com.penguintoast.bloodline.net.GameClient;
import com.penguintoast.bloodline.net.GameServer;
import com.penguintoast.bloodline.net.Network;

public class LobbyScreen extends BaseScreen {
	private GameClient client;
	private GameServer server;
	private PlayerList playerList;
	private ChatBox chat;

	public LobbyScreen(GameClient client) {
		this.client = client;
		init();
	}

	public LobbyScreen() {
		init();
	}

	private void init() {
		table.pad(10);

		Table leftTable = new Table();
		table.add(leftTable).expandY().fill().space(10);
		playerList = new PlayerList();
		leftTable.add(playerList).expand().fill().width(250);
		leftTable.row();
		leftTable.add(new TextButton("Lock In", Global.skin)).expandX().fill().space(4f);
		if (Network.host) {
			leftTable.row();
			leftTable.add(new TextButton("Start", Global.skin)).expandX().fill().space(4f);
		}
		leftTable.row();
		TextButton backButton = new TextButton("Back", Global.skin);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(Network.host) {
					server.shutdown();
					Global.game.transition(new MenuScreen());
				} else {
					client.shutdown();
					Global.game.transition(new JoinScreen());
				}
			}
		});
		leftTable.add(backButton).expandX().fill().space(4f);

		Table rightTable = new Table();
		table.add(rightTable).expand().fill().space(10);
		MapSelector map = new MapSelector();
		rightTable.add(map).expand().fill();
		rightTable.row();
		BloodlineSelector bloodline = new BloodlineSelector();
		rightTable.add(bloodline).expand().fill();

		table.row();
		chat = new ChatBox();
		table.add(chat).expandX().fill().colspan(2).height(200).space(10);

		if (Network.host) {
			server = new GameServer(this);

			chat.setServer(server);
			server.start();
		} else {
			chat.setClient(client);
			client.setLobby(this);
		}
	}

	@Override
	public void show() {
		super.show();
		if (!Network.host) {
			for (PlayerData p : Network.players.values()) {
				playerJoined(p);
			}
		}
	}

	public void showErrorDialog() {
		Dialog errorDialog = new Dialog("", Global.skin) {
			@Override
			protected void result(Object object) {
				Global.game.transition(new JoinScreen());
			}
		};
		errorDialog.setSkin(Global.skin);
		errorDialog.getContentTable().add("Connection lost.");

		Button but = new TextButton("Ok", Global.skin);
		but.pad(0, 20, 3, 20);
		errorDialog.button(but);
		errorDialog.setMovable(true);
		errorDialog.pad(10f);
		errorDialog.show(stage);
	}

	public void playerJoined(PlayerData data) {
		playerList.addPlayer(data);
	}

	public void playerLeft(int id) {
		playerList.removePlayer(id);
	}

	public void chat(String message) {
		chat.append(message);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		Table.drawDebug(stage);
	}

}
