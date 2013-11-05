package com.penguintoast.bloodline;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class Main {
	public static void main(String[] args) {
		TexturePacker2.process("../pack", "../BloodlinePenguins-android/assets/", "game");
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BloodlinePenguins";
		cfg.useGL20 = false;
		cfg.width = 600;
		cfg.height = 600;
		
		new LwjglApplication(new BloodlinePenguinsMain(), cfg);
	}
}
