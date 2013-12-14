package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class StaticVisualEffect extends VisualEffect {
	protected TextureRegion texture;

	public StaticVisualEffect(TextureRegion texture) {
		super();
		this.texture = texture;
		size.x = texture.getRegionWidth();
		size.y = texture.getRegionHeight();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void renderOverride(Batch batch, float x, float y) {
		float xx = x + offset.x - origin.x;
		float yy = y + offset.y - origin.y;
		float sx = scale.x;
		float sy = scale.y;
		float r = rotation;

		batch.draw(texture,
				xx, yy, // Location
				origin.x, origin.y, // Origin
				size.x, size.y, // Size
				sx, sy, // Scale
				r // Rotation
		);
	}

	@Override
	public void renderOverride(Batch batch, float x, float y, float originX, float originY, float scaleX, float scaleY, float rotation) {
		float sx = scale.x;
		float sy = scale.y;
		float r = this.rotation;

		sx *= scaleX;
		sy *= scaleY;
		r += rotation;

		float dx = offset.x - originX;
		float dy = offset.y - originY;
		dx *= scaleX;
		dy *= scaleY;

		float nx = dx;
		float ny = dy;
		
		if (rotation != 0) {
			nx = dx * MathUtils.cosDeg(rotation) - dy * MathUtils.sinDeg(rotation);
			ny = dx * MathUtils.sinDeg(rotation) + dy * MathUtils.cosDeg(rotation);
		}

		float xx = nx + (x) - origin.x;
		float yy = ny + (y) - origin.y;

		batch.draw(texture,
				xx, yy, // Location
				origin.x, origin.y, // Origin
				size.x, size.y, // Size
				sx, sy, // Scale
				r // Rotation
		);
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void start() {
	}
	
	@Override
	public VisualEffect newInstance() {
		VisualEffect out = new StaticVisualEffect(texture);
		VisualEffect.copyBasic(this, out);
		return out;
	}

}
