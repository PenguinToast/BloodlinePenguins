package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StaticVisualEffect extends VisualEffect {
	protected TextureRegion texture;
	
	public StaticVisualEffect(TextureRegion texture) {
		this.texture = texture;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(Batch batch, float x, float y) {
		batch.draw(texture, x, y);
	}

}
