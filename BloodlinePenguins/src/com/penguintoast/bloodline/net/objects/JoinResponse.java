package com.penguintoast.bloodline.net.objects;

import com.badlogic.gdx.utils.Array;
import com.penguintoast.bloodline.data.PlayerData;

public class JoinResponse {
	public Array<PlayerData> players;
	public byte response;
	public int pID;
	
	public static transient final byte ACCEPTED = 1, FULL = 2, BANNED = 3;

	public JoinResponse() {
	}
	
	public JoinResponse(Array<PlayerData> players, int pID) {
		this.players = players;
		this.pID = pID;
		response = ACCEPTED;
	}

}
