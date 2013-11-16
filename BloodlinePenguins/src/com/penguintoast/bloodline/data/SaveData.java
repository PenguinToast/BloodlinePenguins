package com.penguintoast.bloodline.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveData {
	private static Preferences pref;
	
	static {
		pref = Gdx.app.getPreferences("BPSave");
	}

	public static String getName() {
		return pref.getString("name", getCompName());
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
