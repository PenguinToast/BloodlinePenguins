package com.penguintoast.bloodline.data;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;

public class InputSaveData {
	private IntMap<InputKey> keys;

	private InputSaveData() {
		keys = new IntMap<InputKey>(InputKey.values().length);
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Entry<InputKey> entry : keys.entries()) {
			out.append(Integer.toString(entry.key) + "," + entry.value.toString() + ";");
		}
		out.deleteCharAt(out.length());
		return out.toString();
	}

	public static InputSaveData fromString(String input) {
		InputSaveData out = new InputSaveData();
		if (input == null) {
			InputKey[] inputValues = InputKey.values();
			for (InputKey pi : inputValues) {
				out.keys.put(pi.getDefaultKey(), pi);
			}
		} else {
			try {
				String[] tokens = input.split(";");
				for (String token : tokens) {
					String[] parts = token.split(",");
					out.keys.put(Integer.parseInt(parts[0]), InputKey.valueOf(parts[1]));
				}
			} catch (Exception ex) {
				return InputSaveData.fromString(null);
			}
		}
		return out;
	}
	
	public InputKey getMapping(int key) {
		return keys.get(key);
	}
}
