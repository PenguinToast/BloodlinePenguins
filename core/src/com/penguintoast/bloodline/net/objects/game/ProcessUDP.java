package com.penguintoast.bloodline.net.objects.game;

import java.util.ArrayDeque;

public class ProcessUDP {
	public ArrayDeque<Object> objects = new ArrayDeque<Object>();
	
	public ProcessUDP() {
		
	}
	
	public ProcessUDP(ArrayDeque<Object> data) {
		objects = data;
	}
}
