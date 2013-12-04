package com.penguintoast.bloodline.ui.screens;

import java.util.ArrayDeque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.penguintoast.bloodline.actors.GameActor;
import com.penguintoast.bloodline.data.InputKey;
import com.penguintoast.bloodline.data.SaveData;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.net.objects.game.GameActorCreate;
import com.penguintoast.bloodline.net.objects.game.GameActorDestroy;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;

public class GameScreen extends BaseScreen {
	private Stage gameStage;

	public GameScreen() {
		gameStage = new Stage();
		gameStage.setCamera(stage.getCamera());

		table.setBackground((Drawable) null);

		stage.addListener(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				InputKey key = SaveData.getInput().getMapping(keycode);
				if (key != null) {
					Network.updatePlayerInput(key, true);
				}
				return false;
			}
			
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				InputKey key = SaveData.getInput().getMapping(keycode);
				if (key != null) {
					Network.updatePlayerInput(key, false);
				}
				return false;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				InputKey key = SaveData.getInput().getMapping(-2 - button);
				if (key != null) {
					Network.updatePlayerInput(key, true);
				}
				return false;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				InputKey key = SaveData.getInput().getMapping(-2 - button);
				if (key != null) {
					Network.updatePlayerInput(key, false);
				}
			}
			
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				Network.updatePlayerMouse(new Vector2(x, y));
			}

			@Override
			public boolean mouseMoved(InputEvent event, float x, float y) {
				Network.updatePlayerMouse(new Vector2(x, y));
				return false;
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		update();

		gameStage.act(delta);
		gameStage.draw();

		stage.act(delta);
		stage.draw();
	}

	private void update() {
		if (Network.host) {
			Network.server.updateData();
		}

		Object o;
		while((o = Network.processTCP.poll()) != null) {
			process(o);
		}
		while ((o = Network.processUDP.poll()) != null) {
			process(o);
		}
	}

	@SuppressWarnings("unchecked")
	private void process(Object o) {
		if (o instanceof GameActorCreate) {
			GameActorCreate data = (GameActorCreate) o;
			GameActor actor = data.create();
			actor.setID(data.id);
			actor.setOwner(data.owner);
			Network.actors.put(data.id, actor);
			gameStage.addActor(actor);
		}
		if (o instanceof GameActorDestroy) {
			long id = ((GameActorDestroy) o).id;
			Network.actors.remove(id).remove();
		}
		if (o instanceof GameActorUpdate && !Network.host) {
			GameActorUpdate data = (GameActorUpdate) o;
			GameActor actor = Network.actors.get(data.id);
			if (actor != null) {
				actor.processData(data);
			}
		}
		if (o instanceof ArrayDeque) {
			Object j;
			while((j = ((ArrayDeque<Object>) o).poll()) != null) {
				process(j);
			}
		}
	}
}
