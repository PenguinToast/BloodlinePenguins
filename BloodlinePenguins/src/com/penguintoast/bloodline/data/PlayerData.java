package com.penguintoast.bloodline.data;

public class PlayerData {
	private static transient PlayerData instance;
	public String name;
	public int id;

	public static PlayerData getInstance() {
		if(instance == null) {
			instance = new PlayerData();
			instance.name = SaveData.getName();
		}
		return instance;
	}
	
	public static void updateInstance(PlayerData update) {
		if(instance != null) {
			instance = update;
		}
	}

}
