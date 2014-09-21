package com.penguintoast.bloodline.data;

import com.badlogic.gdx.Input.Keys;

public enum InputKey {
	/**
	 * LMB
	 */
	ABILITY1(InputKey.LMB),
	/**
	 * RMB
	 */
	ABILITY2(InputKey.RMB),
	/**
	 * SPACE
	 */
	ABILITY3(Keys.SPACE),
	/**
	 * Q
	 */
	ABILITY4(Keys.Q),
	/**
	 * E
	 */
	ABILITY5(Keys.E),
	/**
	 * F
	 */
	ABILITY6(Keys.F),
	/**
	 * W
	 */
	MOVEUP(Keys.W),
	/**
	 * S
	 */
	MOVEDOWN(Keys.S),
	/**
	 * A
	 */
	MOVELEFT(Keys.A),
	/**
	 * D
	 */
	MOVERIGHT(Keys.D);
	
	public static final int LMB = -2, RMB = -3, MMB = -4;
	
	private final int defaultKey;
	
	private InputKey(int defaultKey) {
		this.defaultKey = defaultKey;
	}
	
	public int getDefaultKey() {
		return defaultKey;
	}
}
