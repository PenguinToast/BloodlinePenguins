package com.penguintoast.bloodline.net.objects;

import com.penguintoast.bloodline.data.PlayerData;

public class PlayerJoined {
	public PlayerData player;

	public PlayerJoined() {
	}
	
	public PlayerJoined(PlayerData data) {
		player = data;
	}

}
