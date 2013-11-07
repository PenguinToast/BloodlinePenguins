package com.penguintoast.bloodline;

import com.badlogic.gdx.Game;
import com.penguintoast.bloodline.gui.screens.LoadingScreen;

public class BloodlinePenguinsMain extends Game {
	
	@Override
	public void create() {
		Global.game = this;
		setScreen(new LoadingScreen());
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		
		super.render();
	}
}
