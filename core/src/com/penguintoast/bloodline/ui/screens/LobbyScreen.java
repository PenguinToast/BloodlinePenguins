package com.penguintoast.bloodline.ui.screens;

import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.GameServer;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.ui.widgets.BloodlineSelector;
import com.penguintoast.bloodline.ui.widgets.ChatBox;
import com.penguintoast.bloodline.ui.widgets.MapSelector;
import com.penguintoast.bloodline.ui.widgets.PlayerList;

public class LobbyScreen extends BaseScreen {
	private PlayerList playerList;
	private ChatBox chat;
	private TextButton startButton;

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
		
		TextButton lockButton = new TextButton("Lock In", Global.skin);
		lockButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				((TextButton) actor).setDisabled(true);
				if(Network.host) {
					Network.server.ready();
				} else {
					Network.client.ready();
				}
			}
		});
		leftTable.add(lockButton).expandX().fill().space(4f);
		
		if (Network.host) {
			leftTable.row();
			startButton = new TextButton("Start", Global.skin);
			startButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					Network.server.startGame();
				}
			});
			startButton.setDisabled(true);
			leftTable.add(startButton).expandX().fill().space(4f);
		}
		leftTable.row();
		
		TextButton backButton = new TextButton("Back", Global.skin);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(Network.host) {
					Network.server.shutdown();
					Global.game.transition(new MenuScreen());
				} else {
					Network.client.shutdown();
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
			Network.server = new GameServer(this);
			Network.server.start();
		} else {
			Network.client.setLobby(this);
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
	
	public void playerReady(int id) {
		playerList.ready(id);
	}

	@Override
	public void render(float delta) {
		if(Network.host) {
			boolean ready = true;
			Iterator<PlayerData> it = Network.players.values().iterator();
			while(it.hasNext()) {
				if(!it.next().ready) {
					ready = false;
				}
			}
			if(ready) {
				startButton.setDisabled(false);
			} else {
				startButton.setDisabled(true);
			}
		}
		
		super.render(delta);
	}

}
