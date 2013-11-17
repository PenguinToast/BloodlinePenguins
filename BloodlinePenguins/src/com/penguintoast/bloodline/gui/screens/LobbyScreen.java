package com.penguintoast.bloodline.gui.screens;

import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.widgets.PlayerList;
import com.penguintoast.bloodline.net.GameClient;
import com.penguintoast.bloodline.net.Network;

public class LobbyScreen extends BaseScreen {
	private GameClient client;
	private PlayerList playerList;

	public LobbyScreen(GameClient client) {
		this.client = client;
		client.setLobby(this);

		playerList = new PlayerList();
		table.add(playerList).size(200, 400).expand().top().left();
	}
	
	@Override
	public void show() {
		super.show();
		Network.host = false;
		for(PlayerData p : Network.players.values()) {
			playerJoined(p);
		}
	}
	
	public void playerJoined(PlayerData data) {
		playerList.addPlayer(data);
	}
	
	public void playerLeft(int id) {
		playerList.removePlayer(id);
	}

	
}
