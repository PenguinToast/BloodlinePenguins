package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.widgets.PlayerList;
import com.penguintoast.bloodline.net.GameClient;
import com.penguintoast.bloodline.net.Network;

public class LobbyScreen extends BaseScreen {
	private GameClient client;
	private PlayerList playerList;

	public LobbyScreen(GameClient client) {
		Table table = new Table(Global.skin);
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground("background");
		
		this.client = client;

		playerList = new PlayerList();
		table.add(playerList).size(200, 400).expand().top().left();
	}
	
	@Override
	public void show() {
		super.show();
		Network.host = false;
		client.setLobby(this);
		playerList.addPlayer(PlayerData.getInstance());
	}
	
	public void playerJoined(PlayerData data) {
		playerList.addPlayer(data);
	}
	
	public void playerLeft(int id) {
		playerList.removePlayer(id);
	}

	
}
