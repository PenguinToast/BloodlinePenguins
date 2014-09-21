package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class AnimationVisualEffect extends VisualEffect {
	public static final float FRAME_DURATION = 1/60f;
	
	protected float timer;
	protected Animation animation;
	protected Array<? extends TextureRegion> textures;

	public AnimationVisualEffect(Animation animation, Array<? extends TextureRegion> textures) {
		super();
		this.animation = animation;
		TextureRegion texture = animation.getKeyFrame(0);
		size.x = texture.getRegionWidth();
		size.y = texture.getRegionHeight();
		this.textures = textures;
	}

	@Override
	public void update(float delta) {
		timer += delta;
	}

	@Override
	public void renderOverride(Batch batch, float x, float y) {
		float xx = x + offset.x - origin.x;
		float yy = y + offset.y - origin.y;
		float sx = scale.x;
		float sy = scale.y;
		float r = rotation;

		batch.draw(animation.getKeyFrame(timer),
				xx, yy, // Location
				origin.x, origin.y, // Origin
				size.x, size.y, // Size
				sx, sy, // Scale
				r // Rotation
		);
	}

	@Override
	public void renderOverride(Batch batch, float x, float y, float originX, float originY, float scaleX, float scaleY, float rotation) {
		float xx = x + offset.x - origin.x;
		float yy = y + offset.y - origin.y;
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

		xx = nx + (x) - origin.x;
		yy = ny + (y) - origin.y;

		batch.draw(animation.getKeyFrame(timer),
				xx, yy, // Location
				origin.x, origin.y, // Origin
				size.x, size.y, // Size
				sx, sy, // Scale
				r // Rotation
		);
	}
	
	@Override
	public void start() {
		timer = 0;
	}
	
	@Override
	public boolean isFinished() {
		return (animation.getPlayMode() == PlayMode.NORMAL || animation.getPlayMode() == PlayMode.REVERSED) ? animation.isAnimationFinished(timer) : false;
	}
	
	@Override
	public VisualEffect newInstance() {
		VisualEffect out = new AnimationVisualEffect(new Animation(animation.getFrameDuration(), textures, animation.getPlayMode()), textures);
		VisualEffect.copyBasic(this, out);
		return out;
	}
}
