package com.penguintoast.bloodline.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.penguintoast.bloodline.net.listener.ServerListener;

public class GameServer {
	private Server server;

	public GameServer() {

	}

	public void start() {
		try {
			server = new Server() {
				@Override
				protected Connection newConnection() {
					return new GameConnection();
				}
			};

			Network.register(server);

			server.addListener(new ServerListener());

			server.bind(Network.TCP_PORT, Network.UDP_PORT);
			server.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
