package com.penguintoast.bloodline.actors.bloodlines;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.actors.GameActor;
import com.penguintoast.bloodline.actors.vfx.VisualEffect;
import com.penguintoast.bloodline.data.InputKey;
import com.penguintoast.bloodline.net.Network;
import com.penguintoast.bloodline.net.objects.game.GameActorCreate;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;

public class Bloodline extends GameActor {
	
	public Bloodline() {
		visual = Global.assets.get("assets/data/bloodline/test_bloodline/idle.vfx", VisualEffect.class).newInstance();
		setPosition(100, 100);
		visual.start();
	}

	@Override
	public void processData(GameActorUpdate data) {
		Vector2 loc = ((BloodlineUpdate) data).loc;
		setPosition(loc.x, loc.y);
		setRotation(((BloodlineUpdate) data).rot);
	}

	@Override
	public void actServer(float delta) {
		float speed = 150;
		if (Network.players.get(owner).input.get(InputKey.MOVEUP)) {
			float dy = speed * delta;
			setY(getY() + dy);
		}
		if (Network.players.get(owner).input.get(InputKey.MOVEDOWN)) {
			float dy = speed * delta;
			setY(getY() - dy);
		}
		if (Network.players.get(owner).input.get(InputKey.MOVERIGHT)) {
			float dx = speed * delta;
			setX(getX() + dx);
		}
		if (Network.players.get(owner).input.get(InputKey.MOVELEFT)) {
			float dx = speed * delta;
			setX(getX() - dx);
		}
		
		Vector2 mouse = Network.players.get(owner).mouseLoc;
		setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(mouse.y - getY(), mouse.x - getX()) - 90);
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
		public float rot;

		public BloodlineUpdate() {

		}

		public BloodlineUpdate(Bloodline b) {
			this.id = b.id;
			this.loc = new Vector2(b.getX(), b.getY());
			this.rot = b.getRotation();
		}
	}

	public static void register(Kryo k) {
		k.register(BloodlineCreate.class);
		k.register(BloodlineUpdate.class);
	}

}
