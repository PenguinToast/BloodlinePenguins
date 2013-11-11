package com.penguintoast.bloodline.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.screens.HostScreen;
import com.penguintoast.bloodline.net.listener.ServerListener;

public class GameServer {
	private Server server;
	private HostScreen screen;

	public GameServer(HostScreen screen) {
		this.screen = screen;
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

			server.addListener(new ServerListener(this));

			server.bind(Network.TCP_PORT, Network.UDP_PORT);
			server.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	
	public void playerJoined(PlayerData data) {
		screen.playerJoined(data);
	}
	
	public void playerLeft(PlayerData data) {
		screen.playerLeft(data);
	}
	
	public Server getServer() {
		return server;
	}

}
