package com.penguintoast.bloodline.net.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.penguintoast.bloodline.net.GameClient;
import com.penguintoast.bloodline.net.objects.InfoResponse;

public class ClientListener extends Listener {
	private GameClient client;
	
	public ClientListener(GameClient gameClient) {
		this.client = gameClient;
	}

	@Override
	public void received(Connection connection, Object object) {
		System.out.println("Client: object recieved: " + object.getClass().getName());
		if(object instanceof InfoResponse) {
			client.received(object);
		}
	}
}
