package com.penguintoast.bloodline.loader;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

public class DataFile {
	private ObjectMap<String, String> data;
	private boolean inArray;
	private String arrayName;
	private String array;
	
	public DataFile(FileHandle handle) {
		data = new ObjectMap<String, String>();
		parse(handle);
	}
	
	public void parse(FileHandle handle) {
		try {
			BufferedReader br = handle.reader(4096);
			String line = null;
			while((line = br.readLine()) != null) {
				parse(line);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void parse(String line) {
		line = line.trim();
		if(line.startsWith("//")) {
			return;
		}
		
		if(inArray) {
			if(line.equals("}")) {
				inArray = false;
				data.put(arrayName, array);
				return;
			}
			array += line;
			return;
		}
		
		String[] parts = line.split(":");
		if(parts.length <= 1) {
			return;
		}
		
		String pt0 = parts[0].trim();
		String pt1 = parts[1].trim();
		if(pt1.equals("{")) {
			inArray = true;
			arrayName = pt0;
			array = "";
		} else {
			data.put(pt0, pt1);
		}
	}
	
	public String get(String key) {
		return data.get(key);
	}
}
