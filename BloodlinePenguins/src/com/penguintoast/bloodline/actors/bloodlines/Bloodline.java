package com.penguintoast.bloodline.actors.bloodlines;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.actors.GameActor;
import com.penguintoast.bloodline.actors.vfx.AnimationVisualEffect;
import com.penguintoast.bloodline.data.InputKey;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.net.objects.game.GameActorCreate;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;

public class Bloodline extends GameActor {

	public Bloodline() {
		//visual = new StaticVisualEffect(Global.atlas.findRegion("test_bloodline"));
		visual = new AnimationVisualEffect(new Animation(1/60f, Global.atlas.findRegions("test_bloodline_abil1"), Animation.LOOP));
	}

	@Override
	public void processData(GameActorUpdate data) {
		Vector2 loc = ((BloodlineUpdate) data).loc;
		setPosition(loc.x, loc.y);
	}

	@Override
	public void actServer(float delta) {
		if(Network.players.get(owner).input.get(InputKey.MOVEUP)) {
			translate(0, 1);
		}
		Network.updateActorUDP(new BloodlineUpdate(this));
	}

	@Override
	public void actClient(float delta) {
		
	}
	
	public static class BloodlineCreate extends GameActorCreate {
		
		public BloodlineCreate() {
			
		}
		
		public BloodlineCreate(int owner) {
			this.owner = owner;
		}
		
		@Override
		public GameActor create() {
			Bloodline out = new Bloodline();
			return out;
		}
		
	}
	
	public static class BloodlineUpdate extends GameActorUpdate {
		public Vector2 loc;
		
		public BloodlineUpdate() {
			
		}
		
		public BloodlineUpdate(Bloodline b) {
			this.id = b.id;
			this.loc = new Vector2(b.getX(), b.getY());
		}
	}
	
	public static void register(Kryo k) {
		k.register(BloodlineCreate.class);
		k.register(BloodlineUpdate.class);
	}

}
