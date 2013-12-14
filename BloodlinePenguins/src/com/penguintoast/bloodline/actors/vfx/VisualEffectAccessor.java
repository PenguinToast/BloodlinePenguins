package com.penguintoast.bloodline.actors.vfx;

import aurelienribon.tweenengine.TweenAccessor;

public class VisualEffectAccessor implements TweenAccessor<VisualEffect> {

	public static final int OFFSET_X = 1;
	public static final int OFFSET_Y = 2;
	public static final int OFFSET_XY = 3;
	public static final int ALPHA = 4;
	public static final int SCALE_X = 5;
	public static final int SCALE_Y = 6;
	public static final int SCALE_XY = 7;
	public static final int ROTATION = 8;
	public static final int ORIGIN_X = 9;
	public static final int ORIGIN_Y = 10;
	public static final int ORIGIN_XY = 11;
	
	public static int getTweenType(String type) {
		if(type.equalsIgnoreCase("OFFSET_X")) {
			return OFFSET_X;
		} else if(type.equalsIgnoreCase("OFFSET_Y")) {
			return OFFSET_Y;
		}else if(type.equalsIgnoreCase("OFFSET_XY")) {
			return OFFSET_XY;
		} else if(type.equalsIgnoreCase("ALPHA")) {
			return ALPHA;
		} else if(type.equalsIgnoreCase("SCALE_X")) {
			return SCALE_X;
		} else if(type.equalsIgnoreCase("SCALE_Y")) {
			return SCALE_Y;
		} else if(type.equalsIgnoreCase("SCALE_XY")) {
			return SCALE_XY;
		} else if(type.equalsIgnoreCase("ROTATION")) {
			return ROTATION;
		} else if(type.equalsIgnoreCase("ORIGIN_X")) {
			return ORIGIN_X;
		} else if(type.equalsIgnoreCase("ORIGIN_Y")) {
			return ORIGIN_Y;
		} else if(type.equalsIgnoreCase("ORIGIN_XY")) {
			return ORIGIN_XY;
		}
		return -1;
	}

	@Override
	public int getValues(VisualEffect target, int tweenType, float[] returnValues) {
		switch(tweenType) {
			case OFFSET_X:
				returnValues[0] = target.getOffset().x;
				return 1;
			case OFFSET_Y:
				returnValues[0] = target.getOffset().y;
				return 1;
			case OFFSET_XY:
				returnValues[0] = target.getOffset().x;
				returnValues[1] = target.getOffset().y;
				return 2;
			case ALPHA:
				returnValues[0] = target.getAlpha();
				return 1;
			case SCALE_X:
				returnValues[0] = target.getScale().x;
				return 1;
			case SCALE_Y:
				returnValues[0] = target.getScale().y;
				return 1;
			case SCALE_XY:
				returnValues[0] = target.getScale().x;
				returnValues[1] = target.getScale().y;
				return 2;
			case ROTATION:
				returnValues[0] = target.getRotation();
				return 1;
			case ORIGIN_X:
				returnValues[0] = target.getOrigin().x;
				return 1;
			case ORIGIN_Y:
				returnValues[0] = target.getOrigin().y;
				return 1;
			case ORIGIN_XY:
				returnValues[0] = target.getOrigin().x;
				returnValues[1] = target.getOrigin().y;
				return 2;
			default:
				return -1;
		}
	}

	@Override
	public void setValues(VisualEffect target, int tweenType, float[] newValues) {
		switch(tweenType) {
			case OFFSET_X:
				target.getOffset().x = newValues[0];
				break;
			case OFFSET_Y:
				target.getOffset().y = newValues[0];
				break;
			case OFFSET_XY:
				target.getOffset().x = newValues[0];
				target.getOffset().y = newValues[1];
				break;
			case ALPHA:
				target.setAlpha(newValues[0]);
				break;
			case SCALE_X:
				target.getScale().x = newValues[0];
				break;
			case SCALE_Y:
				target.getScale().y = newValues[0];
				break;
			case SCALE_XY:
				target.getScale().x = newValues[0];
				target.getScale().y = newValues[1];
				break;
			case ROTATION:
				target.setRotation(newValues[0]);
				break;
			case ORIGIN_X:
				target.getOrigin().x = newValues[0];
				break;
			case ORIGIN_Y:
				target.getOrigin().y = newValues[0];
				break;
			case ORIGIN_XY:
				target.getOrigin().x = newValues[0];
				target.getOrigin().y = newValues[1];
				break;
			default:
				break;
		}
	}

}
