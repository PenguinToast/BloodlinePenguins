package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.penguintoast.bloodline.Global;

public class MenuScreen implements Screen {
	private Stage stage;

	public MenuScreen() {
		OrthographicCamera camera = new OrthographicCamera(Global.WIDTH, Global.HEIGHT);

		stage = new Stage();
		stage.setCamera(camera);
		
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table(Global.skin);
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground("background");

		TextButton btn = new TextButton("Start Game", Global.skin);
		TextButton btn2 = new TextButton("Options", Global.skin);
		table.add(btn.pad(-3, 3, 0, 3)).space(4);
		table.row();
		table.add(btn2.pad(-3, 3, 0, 3));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(Global.WIDTH, Global.HEIGHT, width, height);
		int viewportX = (int) (width - size.x) / 2;
		int viewportY = (int) (height - size.y) / 2;
		int viewportWidth = (int) size.x;
		int viewportHeight = (int) size.y;
		Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
		stage.setViewport(Global.WIDTH, Global.HEIGHT, true, viewportX, viewportY, viewportWidth, viewportHeight);
		
		System.out.println(viewportWidth);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
