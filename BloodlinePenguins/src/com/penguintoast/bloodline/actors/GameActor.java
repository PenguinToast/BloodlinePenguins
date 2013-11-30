package com.penguintoast.bloodline.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryo.Kryo;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;

public abstract class GameActor extends Actor {
	protected long id;
	
	public static void register(Kryo k) {
		
	}
	
	public abstract void processData(GameActorUpdate data);
	
	public long getID() {
		return id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
}
