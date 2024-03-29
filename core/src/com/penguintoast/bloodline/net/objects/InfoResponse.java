package com.penguintoast.bloodline.net.objects;

import java.net.InetAddress;
import java.util.Arrays;

public class InfoResponse {
	public byte[] hwID;
	public String name;
	public transient InetAddress address;

	public InfoResponse(byte[] hwID, String name) {
		this.hwID = hwID;
		this.name = name;
	}
	
	public InfoResponse() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && Arrays.equals(this.hwID, ((InfoResponse)obj).hwID);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
