package com.penguintoast.bloodline;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class Main {
	public static void main(String[] args) {
		TexturePacker2.process("../assets/load", "../BloodlinePenguins-android/assets/", "game");
		TexturePacker2.process("../assets/preload", "../BloodlinePenguins-android/assets/", "preload");
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BloodlinePenguins";
		cfg.useGL20 = true;
		cfg.width = (int) Global.WIDTH;
		cfg.height = (int) Global.HEIGHT;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new BloodlinePenguinsMain(), cfg);
	}
}
