package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class VisualEffect {
	public abstract void update(float delta);

	public abstract void render(Batch batch, float x, float y);
	
}
