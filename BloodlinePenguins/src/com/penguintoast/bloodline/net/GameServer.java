package com.penguintoast.bloodline.net;

import java.io.IOException;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.penguintoast.bloodline.GameUtil;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.data.SaveData;
import com.penguintoast.bloodline.gui.screens.LobbyScreen;
import com.penguintoast.bloodline.net.listener.ServerListener;
import com.penguintoast.bloodline.net.objects.ChatMessage;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.net.objects.game.ProcessTCP;
import com.penguintoast.bloodline.net.objects.game.ProcessUDP;
import com.penguintoast.bloodline.net.objects.lobby.PlayerJoined;
import com.penguintoast.bloodline.net.objects.lobby.PlayerLeft;
import com.penguintoast.bloodline.net.objects.lobby.PlayerReady;

public class GameServer {
	private Server server;
	private LobbyScreen screen;
	private IntArray unused;
	private int playerCount;

	public GameServer(LobbyScreen screen) {
		this.screen = screen;
		unused = new IntArray();
	}

	public void start() {
		try {
			unused.clear();
			playerCount = 1;
			Network.actorCount = 0;
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

			PlayerData.getInstance().id = 0;
			Network.players.put(0, PlayerData.getInstance());
			screen.playerJoined(PlayerData.getInstance());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void shutdown() {
		Network.players.clear();
		Network.actors.clear();
		PlayerData.getInstance().ready = false;
		server.close();
	}

	public void received(GameConnection conn, Object obj) {
		if (obj instanceof InfoRequest) {
			InfoResponse response = new InfoResponse(GameUtil.getHWID(), SaveData.getName());
			conn.sendTCP(response);
		}
		if (obj instanceof PlayerData) {
			PlayerData dat = (PlayerData) obj;
			if (unused.size > 0) {
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
		if (obj instanceof ChatMessage) {
			screen.chat(((ChatMessage) obj).message);
			server.sendToAllTCP(obj);
		}
		if (obj instanceof PlayerReady) {
			int id = conn.getData().id;
			Network.players.get(id).ready = true;
			screen.playerReady(id);
			server.sendToAllTCP(new PlayerReady(id));
		}
	}
	
	public void ready() {
		screen.playerReady(0);
		PlayerData.getInstance().ready = true;
		server.sendToAllTCP(new PlayerReady(0));
	}

	public void chat(String message) {
		screen.chat(message);
		server.sendToAllTCP(new ChatMessage(message));
	}

	public void playerLeft(PlayerData data) {
		unused.add(data.id);
		Network.players.remove(data.id);
		screen.playerLeft(data.id);
		server.sendToAllTCP(new PlayerLeft(data.id));
	}
	
	public void updateData() {
		server.sendToAllTCP(new ProcessTCP(Network.processTCP));
		server.sendToAllUDP(new ProcessUDP(Network.processUDP));
	}

	public Server getServer() {
		return server;
	}

}
