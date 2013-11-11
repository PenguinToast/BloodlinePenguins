package com.penguintoast.bloodline.net.objects;

import java.util.Arrays;

public class InfoResponse {
	public byte[] hwID;
	public String name;

	public InfoResponse(byte[] hwID, String name) {
		this.hwID = hwID;
		this.name = name;
	}
	
	public InfoResponse() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return Arrays.equals(this.hwID, ((InfoResponse)obj).hwID);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
