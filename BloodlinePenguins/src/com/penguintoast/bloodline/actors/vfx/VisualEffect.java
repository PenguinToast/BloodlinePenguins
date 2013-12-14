package com.penguintoast.bloodline.actors.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class VisualEffect {
	protected Vector2 offset, origin, scale, size;
	protected Color color;
	protected float rotation;

	public VisualEffect() {
		offset = new Vector2();
		origin = new Vector2();
		scale = new Vector2(1f, 1f);
		size = new Vector2();
		rotation = 0;
	}

	public abstract void update(float delta);

	public void render(Batch batch, float x, float y) {
		Color tmp = batch.getColor().cpy();
		if (color != null) {
			batch.setColor(batch.getColor().mul(color));
		}
		renderOverride(batch, x, y);
		batch.setColor(tmp);
	}

	public abstract void renderOverride(Batch batch, float x, float y);

	public void render(Batch batch, float x, float y, float originX, float originY, float scaleX, float scaleY, float rotation) {
		Color tmp = batch.getColor().cpy();
		if (color != null) {
			batch.setColor(batch.getColor().mul(color));
		}
		renderOverride(batch, x, y, originX, originY, scaleX, scaleY, rotation);
		batch.setColor(tmp);
	}

	public abstract void renderOverride(Batch batch, float x, float y, float originX, float originY, float scaleX, float scaleY,
			float rotation);

	public abstract void start();

	public abstract boolean isFinished();

	public abstract VisualEffect newInstance();

	public Vector2 getOffset() {
		return offset;
	}

	public void setOffset(Vector2 off) {
		offset = off;
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2 origin) {
		this.origin = origin;
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(Vector2 scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getAlpha() {
		if (color == null) {
			return 1.0f;
		} else {
			return color.a;
		}
	}

	public void setAlpha(float alpha) {
		if (color == null) {
			color = new Color(1.0f, 1.0f, 1.0f, alpha);
		} else {
			color.a = alpha;
		}
	}

	public static void copyBasic(VisualEffect a, VisualEffect b) {
		if (a.color != null) {
			b.color = new Color(a.color);
		}
		b.offset = new Vector2(a.offset);
		b.origin = new Vector2(a.origin);
		b.scale = new Vector2(a.scale);
		b.size = new Vector2(a.size);
		b.rotation = a.rotation;
	}
}
