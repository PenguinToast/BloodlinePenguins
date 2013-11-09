package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.penguintoast.bloodline.Global;

public class TransitionScreen implements Screen {
	private Screen source, target;
	private float timer;
	private float duration = 0.25f;
	private boolean switched;
	private ShapeRenderer renderer;

	public TransitionScreen(Screen source, Screen target) {
		this.source = source;
		this.target = target;
		renderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		timer += delta;
		if (timer <= duration / 2) {
			source.render(delta);
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, timer / (duration / 2));
			renderer.rect(0, 0, Global.WIDTH, Global.HEIGHT);
			renderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		} else if (timer <= duration) {
			if (!switched) {
				target.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				switched = true;
			}
			target.render(delta);
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, 1 - (timer - duration / 2) / (duration / 2));
			renderer.rect(0, 0, Global.WIDTH, Global.HEIGHT);
			renderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		} else {
			Global.game.setScreen(target);
			return;
		}
	}

	@Override
	public void resize(int width, int height) {
		source.resize(width, height);
		target.resize(width, height);
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
