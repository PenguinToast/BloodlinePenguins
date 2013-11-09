package com.penguintoast.bloodline;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.penguintoast.bloodline.gui.screens.LoadingScreen;
import com.penguintoast.bloodline.gui.screens.TransitionScreen;

public class BloodlinePenguinsMain extends Game {
	
	@Override
	public void create() {
		Global.game = this;
		setScreen(new LoadingScreen());
	}
	
	public void transition(Screen target) {
		setScreen(new TransitionScreen(getScreen(), target));
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		
		super.render();
	}
}
