package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.net.GameClient;

public class LobbyScreen extends BaseScreen {
	private GameClient client;

	public LobbyScreen(GameClient client) {
		Table table = new Table(Global.skin);
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground("background");
		
		this.client = client;
		
	}

	
}
