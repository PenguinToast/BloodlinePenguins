package com.penguintoast.bloodline.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.penguintoast.bloodline.BloodlinePenguinsMain;
import com.penguintoast.bloodline.Global;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Global.WIDTH;
		config.height = (int) Global.HEIGHT;
		new LwjglApplication(new BloodlinePenguinsMain(), config);
	}
}
