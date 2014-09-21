package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ParticleVisualEffect extends VisualEffect {
	protected ParticleEffect particle;
	protected TextureRegion texture;
	protected Array<ScaledNumericValue> origAng;
	protected Array<ScaledNumericValue> origScale;
	protected Array<ScaledNumericValue> origVel;

	public ParticleVisualEffect(ParticleEffect particle) {
		super();
		this.particle = particle;
		origAng = new Array<ScaledNumericValue>();
		origScale = new Array<ScaledNumericValue>();
		origVel = new Array<ScaledNumericValue>();
		for (ParticleEmitter pe : particle.getEmitters()) {
			// Angle
			ScaledNumericValue cpy = new ScaledNumericValue();
			cpy.load(pe.getAngle());
			origAng.add(cpy);

			// Scale
			cpy = new ScaledNumericValue();
			cpy.load(pe.getScale());
			origScale.add(cpy);
			// Scale - Velocity
			cpy = new ScaledNumericValue();
			cpy.load(pe.getVelocity());
			origVel.add(cpy);
		}
	}

	@Override
	public void update(float delta) {
		particle.update(delta);
		if (isFinished()) {
			start();
		}
	}

	@Override
	public void renderOverride(Batch batch, float x, float y) {
		float xx = x + offset.x;
		float yy = y + offset.y;
		float sx = scale.x;
		float sy = scale.y;

		float r = rotation;
		
		renderParticle(particle, batch, xx, yy, origin.x, origin.y, sx, sy, r);
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

		float xx = nx + (x);
		float yy = ny + (y);

		particle.setPosition(xx, yy);

		renderParticle(particle, batch, xx, yy, origin.x, origin.y, sx, sy, r);
	}
	
	private void renderParticle(ParticleEffect particle, Batch batch, float x, float y, float originX, float originY, float scaleX, float scaleY, float rotation) {
		float dx = -originX * scaleX;
		float dy = -originY * scaleY;

		float nx = dx;
		float ny = dy;

		if (rotation != 0) {
			nx = dx * MathUtils.cosDeg(rotation) - dy * MathUtils.sinDeg(rotation);
			ny = dx * MathUtils.sinDeg(rotation) + dy * MathUtils.cosDeg(rotation);
		}
		
		float xx = x + nx;
		float yy = y + ny;
		
		particle.setPosition(xx, yy);

		Array<ParticleEmitter> emitters = particle.getEmitters();
		for (int i = 0; i < emitters.size; i++) {
			ParticleEmitter pe = emitters.get(i);
			if (rotation != 0) {
				ScaledNumericValue rot = origAng.get(i);
				rot.setActive(true);
				pe.getAngle().setHigh(rot.getHighMin() + rotation, rot.getHighMax() + rotation);
				pe.getAngle().setLow(rot.getLowMin() + rotation, rot.getLowMax() + rotation);
			}
			if (scaleX != 1.0f) {
				ScaledNumericValue scale = origScale.get(i);
				scale.setActive(true);
				pe.getScale().setHigh(scale.getHighMin() * scaleX, scale.getHighMax() * scaleX);
				pe.getScale().setLow(scale.getLowMin() * scaleX, scale.getLowMax() * scaleX);
				ScaledNumericValue vel = origVel.get(i);
				vel.setActive(true);
				pe.getVelocity().setHigh(vel.getHighMin() * scaleX, vel.getHighMax() * scaleX);
				pe.getVelocity().setLow(vel.getLowMin() * scaleX, vel.getLowMax() * scaleX);
			}
		}

		particle.draw(batch);
	}

	@Override
	public boolean isFinished() {
		return particle.isComplete();
	}

	@Override
	public void start() {
		particle.start();
	}
	
	@Override
	public VisualEffect newInstance() {
		VisualEffect out = new ParticleVisualEffect(new ParticleEffect(particle));
		VisualEffect.copyBasic(this, out);
		return out;
	}
}
