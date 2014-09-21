package com.penguintoast.bloodline.ui.screens;

import java.util.ArrayDeque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.penguintoast.bloodline.Global;
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
		gameStage.getViewport().setCamera(stage.getCamera());

		table.setBackground((Drawable) null);
		Label label = new Label("FPS: ", Global.skin, "textArea") {
			@Override
			public void act(float delta) {
				super.act(delta);
				setText("FPS: " + Gdx.graphics.getFramesPerSecond());
			}
		};
		table.top().add(label).expandX().left();

		if (!Network.host) {
			label = new Label("Ping: ", Global.skin, "textArea") {
				private float timer;
				@Override
				public void act(float delta) {
					super.act(delta);
					timer += delta;
					if(timer >= 1.0f) {
						Network.client.getClient().updateReturnTripTime();
						timer = 0;
					}
					setText("Ping: " + Network.client.getClient().getReturnTripTime());
				}
			};
			table.row();
			table.add(label).expandX().left();
		}

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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		gameStage.act(delta);
		gameStage.draw();

		stage.act(delta);
		stage.draw();
	}

	public void showErrorDialog() {
		Dialog errorDialog = new Dialog("", Global.skin) {
			@Override
			protected void result(Object object) {
				Global.game.transition(new JoinScreen());
			}
		};
		errorDialog.setSkin(Global.skin);
		errorDialog.getContentTable().add("Connection lost.");

		Button but = new TextButton("Ok", Global.skin);
		but.pad(0, 20, 3, 20);
		errorDialog.button(but);
		errorDialog.setMovable(true);
		errorDialog.pad(10f);
		errorDialog.show(stage);

	}

	private void update() {
		if (Network.host) {
			Network.server.updateData();
		}

		Object o;
		while ((o = Network.processTCP.poll()) != null) {
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
			while ((j = ((ArrayDeque<Object>) o).poll()) != null) {
				process(j);
			}
		}
	}
}
