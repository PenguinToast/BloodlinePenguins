package com.penguintoast.bloodline.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.penguintoast.bloodline.Global;

public abstract class BaseScreen implements Screen {
    protected Stage stage;
    protected Table table;

    public BaseScreen() {
        OrthographicCamera camera = new OrthographicCamera(Global.WIDTH, Global.HEIGHT);

        stage = new Stage();
        stage.setViewport(new FitViewport(Global.WIDTH, Global.HEIGHT, camera));
		
        Gdx.input.setInputProcessor(stage);
		
        table = new Table(Global.skin);
        table.setFillParent(true);
        stage.addActor(table);
        table.setBackground("background");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void show() {
		
    }

    @Override
    public void hide() {
		
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
