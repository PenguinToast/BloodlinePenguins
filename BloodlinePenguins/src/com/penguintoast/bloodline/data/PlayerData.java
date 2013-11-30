package com.penguintoast.bloodline.data;

public class PlayerData {
	private static transient PlayerData instance;
	public String name;
	public int id;
	public boolean ready;

	public static PlayerData getInstance() {
		if (instance == null) {
			instance = new PlayerData();
			instance.name = SaveData.getName();
		}
		return instance;
	}

}
