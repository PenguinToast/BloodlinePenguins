package com.penguintoast.bloodline.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveData {
	private static Preferences pref;
	private static String name;
	private static InputSaveData input;
	
	static {
		pref = Gdx.app.getPreferences("BPSave");
		name = pref.getString("name", getCompName());
		input = InputSaveData.fromString(pref.getString("input", null));
	}

	public static String getName() {
		return name;
	}
	
	public static InputSaveData getInput() {
		return input;
	}
	
	private static String getCompName() {
		String out;
		try {
			out = InetAddress.getLocalHost().getHostName();
		} catch(UnknownHostException ex) {
			return "Player1";
		}
		if(out.length() > 14) {
			return out.substring(0, 14);
		} else {
			return out;
		}
	}

}
