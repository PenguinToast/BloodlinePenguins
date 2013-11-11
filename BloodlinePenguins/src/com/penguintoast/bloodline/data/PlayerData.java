package com.penguintoast.bloodline.data;

public class PlayerData {
	public String name;

	public static PlayerData getInstance() {
		PlayerData out = new PlayerData();
		out.name = SaveData.getName();
		return out;
	}

}
