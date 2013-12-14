package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class CombinedVisualEffect extends VisualEffect {
	protected Array<VisualEffect> visuals;

	public CombinedVisualEffect(Array<VisualEffect> effects) {
		this.visuals = effects;
	}

	@Override
	public void update(float delta) {
		for(VisualEffect ve : visuals) {
			ve.update(delta);
		}
	}

	@Override
	public void renderOverride(Batch batch, float x, float y) {
		for(VisualEffect ve : visuals) {
			ve.render(batch, x + offset.x, y + offset.y, origin.x, origin.y, scale.x, scale.y, rotation);
		}
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

		for(VisualEffect ve : visuals) {
			ve.render(batch, xx, yy, origin.x, origin.y, sx, sy, r);
		}
	}

	@Override
	public void start() {
		for(VisualEffect ve : visuals) {
			ve.start();
		}
	}
	
	@Override
	public boolean isFinished() {
		for(VisualEffect ve : visuals) {
			if(!ve.isFinished()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public VisualEffect newInstance() {
		Array<VisualEffect> copy = new Array<VisualEffect>();
		for(VisualEffect ve : visuals) {
			copy.add(ve.newInstance());
		}
		VisualEffect out = new CombinedVisualEffect(copy);
		VisualEffect.copyBasic(this, out);
		return out;
	}

}
