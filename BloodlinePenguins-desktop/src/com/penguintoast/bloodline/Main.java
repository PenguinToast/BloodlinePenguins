package com.penguintoast.bloodline;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class Main {
	private static boolean pack = false;

	public static void main(String[] args) {
		if (pack) {
			TexturePacker2.process("../assets/load", "../BloodlinePenguins-android/assets/", "game");
			TexturePacker2.process("../assets/preload", "../BloodlinePenguins-android/assets/", "preload");
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BloodlinePenguins";
		cfg.useGL20 = false;
		cfg.width = (int) Global.WIDTH;
		cfg.height = (int) Global.HEIGHT;
		cfg.vSyncEnabled = true;
		
		cfg.samples = 2;

		new LwjglApplication(new BloodlinePenguinsMain(), cfg);
	}
}
