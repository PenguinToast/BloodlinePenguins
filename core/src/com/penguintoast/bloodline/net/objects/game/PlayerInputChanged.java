package com.penguintoast.bloodline.net.objects.game;

import com.penguintoast.bloodline.data.InputKey;

public class PlayerInputChanged {
	public InputKey key;
	public boolean state;

	public PlayerInputChanged() {
	}
	
	public PlayerInputChanged(InputKey key, boolean state) {
		this.key = key;
		this.state = state;
	}

}
