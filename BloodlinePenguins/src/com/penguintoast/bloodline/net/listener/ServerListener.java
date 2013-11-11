package com.penguintoast.bloodline.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.penguintoast.bloodline.GameUtil;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.data.SaveData;
import com.penguintoast.bloodline.net.GameConnection;
import com.penguintoast.bloodline.net.GameServer;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;

public class ServerListener extends Listener {
	private GameServer server;

	public ServerListener(GameServer server) {
		this.server = server;
	}

	@Override
	public void received(Connection connection, Object object) {
		GameConnection conn = (GameConnection) connection;
		if (object instanceof InfoRequest) {
			InfoResponse response = new InfoResponse(GameUtil.getHWID(), SaveData.getName());
			conn.sendTCP(response);
		}
		if (object instanceof PlayerData) {
			server.playerJoined((PlayerData) object);
			conn.setData((PlayerData) object);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		GameConnection conn = (GameConnection) connection;
		PlayerData data = conn.getData();
		if (data != null) {
			server.playerLeft(conn.getData());
		}
	}
}
