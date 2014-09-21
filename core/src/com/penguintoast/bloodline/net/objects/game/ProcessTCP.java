package com.penguintoast.bloodline.net.objects.game;

import java.util.ArrayDeque;

public class ProcessTCP {
	public ArrayDeque<Object> objects;
	
	public ProcessTCP() {
		
	}
	
	public ProcessTCP(ArrayDeque<Object> data) {
		objects = data;
	}
}
