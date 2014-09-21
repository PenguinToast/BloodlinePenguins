package com.penguintoast.bloodline.ui.screens;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.actors.vfx.VisualEffect;
import com.penguintoast.bloodline.loader.vfx.VisualEffectLoader;

public class LoadingScreen implements Screen {
	private SpriteBatch batch;
	private Sprite loading;
	private Sprite background;
	private AssetManager assets;
	
	public LoadingScreen() {
		assets = new AssetManager();
		Global.assets = assets;
		
		batch = new SpriteBatch();

		assets.load("assets/preload.atlas", TextureAtlas.class);
		assets.finishLoading();
		TextureAtlas preload = assets.get("assets/preload.atlas", TextureAtlas.class);
		
		loading = new Sprite(preload.findRegion("loading"));
		loading.setPosition(0.5f * (Global.WIDTH - loading.getWidth()), 0.5f * (Global.HEIGHT - loading.getHeight()));
		
		background = new Sprite(preload.findRegion("background"));
		background.setSize(Global.WIDTH, Global.HEIGHT);
		
		assets.load("assets/game.atlas", TextureAtlas.class);
		
		// Load maps
		assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		load(Gdx.files.internal("assets/maps"), TiledMap.class, "tmx");
		
		// Load attrib
		assets.setLoader(VisualEffect.class, new VisualEffectLoader());
		load(Gdx.files.internal("assets/data"), VisualEffect.class, "vfx");
	}
	
	private <T> void load(FileHandle file, Class<T> type, String suffix) {
		try {
			BufferedReader br = file.child("filelist.txt").reader(4096);
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.isEmpty() || !line.endsWith(suffix)) {
					continue;
				}
				System.out.println(line);
				assets.load(new AssetDescriptor<T>(file.child(line), type));
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(float delta) {
		if(assets.isLoaded("assets/game.atlas")) {
			setGlobal();
		}
		
		if(assets.update()) {
			setGlobal();
			Global.game.transition(new MenuScreen());
			dispose();
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		loading.rotate(90 * delta);
		
		batch.begin();
		batch.disableBlending();
		background.draw(batch);
		batch.enableBlending();
		loading.draw(batch);
		batch.end();
	}
	
	private void setGlobal() {
		Global.atlas = assets.get("assets/game.atlas", TextureAtlas.class);
		Global.skin = new Skin(Gdx.files.internal("assets/skin.json"), Global.atlas);
		Global.skin.addRegions(assets.get("assets/preload.atlas", TextureAtlas.class));
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
