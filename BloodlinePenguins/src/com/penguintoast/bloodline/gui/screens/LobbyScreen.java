package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.widgets.ChatBox;
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

		playerList = new PlayerList();
		table.add(playerList).size(200, 400).expand().top().left();

		chat = new ChatBox();
		table.add(chat).size(400, 200);

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

}
