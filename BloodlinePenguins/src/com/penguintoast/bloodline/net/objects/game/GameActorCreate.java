package com.penguintoast.bloodline.net.objects.game;

import com.penguintoast.bloodline.actors.GameActor;

public abstract class GameActorCreate {
	public long id;
	
	public abstract GameActor create();
}
