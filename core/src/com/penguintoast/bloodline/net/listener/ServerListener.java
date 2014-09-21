package com.penguintoast.bloodline.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.GameConnection;
import com.penguintoast.bloodline.net.GameServer;

public class ServerListener extends Listener {
	private GameServer server;

	public ServerListener(GameServer server) {
		this.server = server;
	}

	@Override
	public void received(Connection connection, Object object) {
		if (connection instanceof GameConnection) {
			server.received((GameConnection) connection, object);
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
