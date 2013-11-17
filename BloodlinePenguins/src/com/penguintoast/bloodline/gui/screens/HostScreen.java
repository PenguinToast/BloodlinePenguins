package com.penguintoast.bloodline.gui.screens;

import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.widgets.PlayerList;
import com.penguintoast.bloodline.net.GameServer;
import com.penguintoast.bloodline.net.Network;

public class HostScreen extends BaseScreen {
	private GameServer server;
	private PlayerList playerList;

	public HostScreen() {
		table.pad(10);
		
		playerList = new PlayerList();
		table.add(playerList).size(200, 400).expand().top().left();
	}
	
	@Override
	public void show() {
		Network.host = true;
		server = new GameServer(this);
		
		server.start();
	}
	
	public void playerJoined(PlayerData data) {
		playerList.addPlayer(data);
	}
	
	public void playerLeft(PlayerData data) {
		playerList.removePlayer(data);
	}
	

}
