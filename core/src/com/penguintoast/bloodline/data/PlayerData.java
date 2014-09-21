package com.penguintoast.bloodline.data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class PlayerData {
	private static transient PlayerData instance;
	
	public String name;
	public int id;
	public boolean ready;

	public transient ObjectMap<InputKey, Boolean> input;
	public transient Vector2 mouseLoc;
	
	public PlayerData() {
		InputKey[] inputValues = InputKey.values();
		input = new ObjectMap<InputKey, Boolean>(inputValues.length);
		for(InputKey inputValue : inputValues) {
			input.put(inputValue, false);
		}
		mouseLoc = new Vector2();
	}
	
	public static PlayerData getInstance() {
		if (instance == null) {
			instance = new PlayerData();
			instance.name = SaveData.getName();
		}
		return instance;
	}

}
