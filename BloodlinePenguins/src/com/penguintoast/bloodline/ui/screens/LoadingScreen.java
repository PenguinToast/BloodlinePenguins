package com.penguintoast.bloodline.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.penguintoast.bloodline.Global;

public class LoadingScreen implements Screen {
	private SpriteBatch batch;
	private Sprite loading;
	private Sprite background;
	private AssetManager assets;
	
	public LoadingScreen() {
		assets = new AssetManager();
		Global.assets = assets;
		
		batch = new SpriteBatch();

		assets.load("preload.atlas", TextureAtlas.class);
		assets.finishLoading();
		TextureAtlas preload = assets.get("preload.atlas", TextureAtlas.class);
		
		loading = new Sprite(preload.findRegion("loading"));
		loading.setPosition(0.5f * (Global.WIDTH - loading.getWidth()), 0.5f * (Global.HEIGHT - loading.getHeight()));
		
		background = new Sprite(preload.findRegion("background"));
		background.setSize(Global.WIDTH, Global.HEIGHT);
		
		assets.load("game.atlas", TextureAtlas.class);
		
		// Load maps
		assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		FileHandle mapFolder = Gdx.files.internal("maps");
		for(FileHandle map : mapFolder.list("tmx")) {
			assets.load(new AssetDescriptor<TiledMap>(map, TiledMap.class));
		}
	}

	@Override
	public void render(float delta) {
		if(assets.update()) {
			Global.atlas = assets.get("game.atlas", TextureAtlas.class);
			Global.skin = new Skin(Gdx.files.internal("data/skin.json"), Global.atlas);
			Global.skin.addRegions(assets.get("preload.atlas", TextureAtlas.class));
			Global.game.transition(new MenuScreen());
			dispose();
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		loading.rotate(90 * delta);
		
		batch.begin();
		batch.disableBlending();
		background.draw(batch);
		batch.enableBlending();
		loading.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
