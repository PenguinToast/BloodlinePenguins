package com.penguintoast.bloodline.gui.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.penguintoast.bloodline.actors.GameActor;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.net.objects.game.GameActorCreate;
import com.penguintoast.bloodline.net.objects.game.GameActorDestroy;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;

public class GameScreen extends BaseScreen {
	private Stage gameStage;

	public GameScreen() {
		gameStage = new Stage();
		gameStage.setCamera(stage.getCamera());
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
		if(Network.host) {
			Network.server.updateData();
		}
		
		Iterator<Object> it = Network.processTCP.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			process(o);
			it.remove();
		}
		it = Network.processUDP.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			process(o);
			it.remove();
		}
	}
	
	private void process(Object o) {
		if(o instanceof GameActorCreate) {
			GameActorCreate data = (GameActorCreate) o;
			GameActor actor = data.create();
			actor.setID(data.id);
			Network.actors.put(data.id, actor);
			gameStage.addActor(actor);
		}
		if(o instanceof GameActorDestroy) {
			long id = ((GameActorDestroy) o).id;
			Network.actors.remove(id).remove();
		}
		if(o instanceof GameActorUpdate && !Network.host) {
			GameActorUpdate data = (GameActorUpdate) o;
			GameActor actor = Network.actors.get(data.id);
			if(actor != null) {
				actor.processData(data);
			}
		}
	}
}
