package com.penguintoast.bloodline.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.penguintoast.bloodline.Global;

public class TransitionScreen implements Screen {
	private Screen source, target;
	private float timer;
	private float duration = 0.5f;
	private boolean switched;
	private ShapeRenderer renderer;
	private FitViewport viewport;

	public TransitionScreen(Screen source, Screen target) {
        OrthographicCamera camera = new OrthographicCamera(Global.WIDTH, Global.HEIGHT);
        camera.position.x = Global.WIDTH / 2;
        camera.position.y = Global.HEIGHT / 2;
        camera.update();
        viewport = new FitViewport(Global.WIDTH, Global.HEIGHT, camera);
        
		this.source = source;
		this.target = target;
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(viewport.getCamera().combined);
	}

	@Override
	public void render(float delta) {
		timer += delta;
		if (timer <= duration / 2) {
			source.render(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, timer / (duration / 2));
			renderer.rect(0, 0, Global.WIDTH, Global.HEIGHT);
			renderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		} else if (timer <= duration) {
			if (!switched) {
				target.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				switched = true;
			}
			target.render(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, 1 - (timer - duration / 2) / (duration / 2));
			renderer.rect(0, 0, Global.WIDTH, Global.HEIGHT);	
			renderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		} else {
			Global.game.setScreen(target);
			return;
		}
	}

	@Override
	public void resize(int width, int height) {
	    viewport.update(width, height);
		renderer.setProjectionMatrix(viewport.getCamera().combined);
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
