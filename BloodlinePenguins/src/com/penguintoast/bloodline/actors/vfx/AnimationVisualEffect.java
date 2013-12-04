package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;

public class AnimationVisualEffect extends VisualEffect {
	
	protected float timer;
	protected Animation animation;

	public AnimationVisualEffect(Animation animation) {
		this.animation = animation;
	}

	@Override
	public void update(float delta) {
		timer += delta;
	}

	@Override
	public void render(Batch batch, float x, float y) {
		batch.draw(animation.getKeyFrame(timer), x, y);
	}
}
