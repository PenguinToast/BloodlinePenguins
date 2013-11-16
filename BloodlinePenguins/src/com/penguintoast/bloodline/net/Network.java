package com.penguintoast.bloodline.net;

import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.PlayerJoined;
import com.penguintoast.bloodline.net.objects.PlayerLeft;

public class Network {
	public static final int TCP_PORT = 44443;
	public static final int UDP_PORT = 44444;
	
	public static boolean host = false;
	
	public static IntMap<PlayerData> players = new IntMap<PlayerData>();
	
	public static void register(EndPoint ep) {
		Kryo k = ep.getKryo();
		
		k.register(byte[].class);
		
		k.register(InfoRequest.class);
		k.register(InfoResponse.class);
		k.register(PlayerJoined.class);
		k.register(PlayerLeft.class);
		
		k.register(PlayerData.class);
	}

}
