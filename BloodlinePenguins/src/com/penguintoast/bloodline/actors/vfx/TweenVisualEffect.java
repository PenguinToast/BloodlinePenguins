package com.penguintoast.bloodline.actors.vfx;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

public class TweenVisualEffect extends VisualEffect {
	protected VisualEffect visual;
	protected Timeline tween;
	protected Timeline current;

	static {
		Tween.registerAccessor(VisualEffect.class, new VisualEffectAccessor());
	}

	public TweenVisualEffect(VisualEffect visual, Timeline tween) {
		this.visual = visual;
		this.tween = tween;
	}

	public TweenVisualEffect(VisualEffect visual) {
		this.visual = visual;
	}

	public void setTween(Timeline tween) {
		this.tween = tween;
	}

	@Override
	public void renderOverride(Batch batch, float x, float y) {
		visual.render(batch, x + offset.x, y + offset.y, origin.x, origin.y, scale.x, scale.y, rotation);
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

		visual.render(batch, xx, yy, origin.x, origin.y, sx, sy, r);
	}

	@Override
	public void start() {
		if (current != null) {
			current.free();
		}
		current = copy(tween);
		current.start();
	}

	@Override
	public void update(float delta) {
		if (current != null) {
			current.update(delta);
		}
		visual.update(delta);
	}

	@Override
	public boolean isFinished() {
		return current.isFinished();
	}

	public Timeline copy(Timeline in) {
		Timeline out = Timeline.createSequence();
		for (@SuppressWarnings("rawtypes")
		BaseTween bt : in.getChildren()) {
			Tween t = (Tween) bt;
			Tween tween = Tween.to(t.getTarget(), t.getType(), t.getDuration())
					.targetRelative(t.getTargetValues())
					.ease(t.getEasing())
					.delay(t.getDelay());
			if(t.isYoyo()) {
				tween.repeatYoyo(t.getRepeatCount(), t.getRepeatDelay());
			} else {
				tween.repeat(t.getRepeatCount(), t.getRepeatDelay());
			}
			out.push(tween);
		}
		if(in.isYoyo()) {
			out.repeatYoyo(in.getRepeatCount(), in.getRepeatDelay());
		} else {
			out.repeat(in.getRepeatCount(), in.getRepeatDelay());
		}
		return out;
	}

	@Override
	public VisualEffect newInstance() {
		VisualEffect out = new TweenVisualEffect(visual.newInstance(), copy(tween));
		VisualEffect.copyBasic(this, out);
		return out;
	}
}
