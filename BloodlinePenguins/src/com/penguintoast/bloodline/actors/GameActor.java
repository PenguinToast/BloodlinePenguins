package com.penguintoast.bloodline.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryo.Kryo;
import com.penguintoast.bloodline.actors.bloodlines.Bloodline;
import com.penguintoast.bloodline.actors.vfx.VisualEffect;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;

public abstract class GameActor extends Actor {
	protected long id;
	protected int owner;
	protected VisualEffect visual;
	
	public static void register(Kryo k) {
		Bloodline.register(k);
	}
	
	@Override
	public final void act(float delta) {
		super.act(delta);
		if(!Network.players.containsKey(owner)) {
			Network.destroyActor(id);
			return;
		}
		visual.update(delta);
		if(Network.host) {
			actServer(delta);
		}
		actClient(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		visual.render(batch, getX(), getY());
	}
	
	public void actClient(float delta) {
	}
	
	public void actServer(float delta) {
	}
	
	public void processData(GameActorUpdate data) {
	}
	
	public long getID() {
		return id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public int getOwner() {
		return owner;
	}
	
	public void setOwner(int owner) {
		this.owner = owner;
	}
}
