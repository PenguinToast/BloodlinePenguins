package com.penguintoast.bloodline.net;

import com.esotericsoftware.kryonet.Connection;
import com.penguintoast.bloodline.data.PlayerData;

public class GameConnection extends Connection {
	private PlayerData data;

	public GameConnection() {
		 
	}
	
	public void setData(PlayerData data) {
		this.data = data;
	}
	
	public PlayerData getData() {
		return data;
	}

}
