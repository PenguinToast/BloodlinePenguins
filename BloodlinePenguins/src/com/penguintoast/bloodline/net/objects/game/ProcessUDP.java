package com.penguintoast.bloodline.net.objects.game;

import java.util.LinkedList;

public class ProcessUDP {
	public LinkedList<Object> objects = new LinkedList<Object>();
	
	public ProcessUDP() {
		
	}
	
	public ProcessUDP(LinkedList<Object> data) {
		objects = data;
	}
}
