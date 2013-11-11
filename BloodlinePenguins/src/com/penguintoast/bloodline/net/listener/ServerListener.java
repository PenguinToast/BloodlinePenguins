package com.penguintoast.bloodline.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.penguintoast.bloodline.GameUtil;
import com.penguintoast.bloodline.data.SaveData;
import com.penguintoast.bloodline.net.GameConnection;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;

public class ServerListener extends Listener {
	@Override
	public void received(Connection connection, Object object) {
		GameConnection conn = (GameConnection) connection;
		System.out.println("Server: object recieved: " + object.getClass().getName());
		if(object instanceof InfoRequest) {
			InfoResponse response = new InfoResponse(GameUtil.getHWID(), SaveData.getName());
			conn.sendTCP(response);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		
	}
}
