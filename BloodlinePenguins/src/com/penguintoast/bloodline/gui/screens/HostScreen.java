package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.net.GameServer;

public class HostScreen extends BaseScreen {
	private GameServer server;

	public HostScreen() {
		Table table = new Table(Global.skin);
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground("background");
	}
	
	@Override
	public void show() {
		server = new GameServer();
		
		server.start();
	}

}
