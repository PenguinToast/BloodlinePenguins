package com.penguintoast.bloodline.net;

import java.io.IOException;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.penguintoast.bloodline.GameUtil;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.data.SaveData;
import com.penguintoast.bloodline.gui.screens.HostScreen;
import com.penguintoast.bloodline.net.listener.ServerListener;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.net.objects.PlayerJoined;
import com.penguintoast.bloodline.net.objects.PlayerLeft;

public class GameServer {
	private Server server;
	private HostScreen screen;
	private IntArray unused;
	private int playerCount;

	public GameServer(HostScreen screen) {
		this.screen = screen;
		unused = new IntArray();
	}

	public void start() {
		try {
			unused.clear();
			playerCount = 1;
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
	
	public void received(GameConnection conn, Object obj) {
		if (obj instanceof InfoRequest) {
			InfoResponse response = new InfoResponse(GameUtil.getHWID(), SaveData.getName());
			conn.sendTCP(response);
		}
		if (obj instanceof PlayerData) {
			PlayerData dat = (PlayerData) obj;
			if(unused.size > 0) {
				dat.id = unused.pop();
			} else {
				dat.id = playerCount++;
			}
			Network.players.put(dat.id, dat);
			screen.playerJoined(dat);
			conn.setData(dat);
			conn.sendTCP(new JoinResponse(Network.players.values().toArray(), dat.id));
			server.sendToAllExceptTCP(conn.getID(), new PlayerJoined(dat));
		}
	}
	
	public void playerLeft(PlayerData data) {
		unused.add(data.id);
		Network.players.remove(data.id);
		screen.playerLeft(data);
		server.sendToAllTCP(new PlayerLeft(data.id));
	}
	
	public Server getServer() {
		return server;
	}

}
