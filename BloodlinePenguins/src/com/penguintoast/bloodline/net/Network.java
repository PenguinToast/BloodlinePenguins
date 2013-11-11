package com.penguintoast.bloodline.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;

public class Network {
	public static final int TCP_PORT = 44443;
	public static final int UDP_PORT = 44444;
	
	public static void register(EndPoint ep) {
		Kryo k = ep.getKryo();
		
		k.register(byte[].class);
		
		k.register(InfoRequest.class);
		k.register(InfoResponse.class);
	}

}
