package com.penguintoast.bloodline.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.penguintoast.bloodline.net.GameClient;

public class ClientListener extends Listener {
	private GameClient client;

	public ClientListener(GameClient gameClient) {
		this.client = gameClient;
	}

	@Override
	public void received(Connection connection, Object object) {
		client.received(object);
	}
	
	@Override
	public void disconnected(Connection connection) {
		client.disconnected();
	}
}
