package com.penguintoast.bloodline.net;

import java.io.IOException;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.penguintoast.bloodline.GameUtil;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.actors.bloodlines.Bloodline.BloodlineCreate;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.data.SaveData;
import com.penguintoast.bloodline.net.listener.ServerListener;
import com.penguintoast.bloodline.net.objects.ChatMessage;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.net.objects.game.PlayerInputChanged;
import com.penguintoast.bloodline.net.objects.game.PlayerMouseMoved;
import com.penguintoast.bloodline.net.objects.game.ProcessTCP;
import com.penguintoast.bloodline.net.objects.game.ProcessUDP;
import com.penguintoast.bloodline.net.objects.lobby.GameStart;
import com.penguintoast.bloodline.net.objects.lobby.PlayerJoined;
import com.penguintoast.bloodline.net.objects.lobby.PlayerLeft;
import com.penguintoast.bloodline.net.objects.lobby.PlayerReady;
import com.penguintoast.bloodline.ui.screens.GameScreen;
import com.penguintoast.bloodline.ui.screens.LobbyScreen;

public class GameServer {
	private Server server;
	private LobbyScreen lobby;
	private IntArray unused;
	private int playerCount;

	public GameServer(LobbyScreen screen) {
		this.lobby = screen;
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
			lobby.playerJoined(PlayerData.getInstance());
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
			lobby.playerJoined(dat);
			conn.setData(dat);
			conn.sendTCP(new JoinResponse(Network.players.values().toArray(), dat.id));
			server.sendToAllExceptTCP(conn.getID(), new PlayerJoined(dat));
		}
		if (obj instanceof ChatMessage) {
			lobby.chat(((ChatMessage) obj).message);
			server.sendToAllTCP(obj);
		}
		if (obj instanceof PlayerReady) {
			int id = conn.getData().id;
			Network.players.get(id).ready = true;
			lobby.playerReady(id);
			server.sendToAllTCP(new PlayerReady(id));
		}
		// Player Input
		if (obj instanceof PlayerInputChanged) {
			PlayerInputChanged data = (PlayerInputChanged) obj;
			conn.getData().input.put(data.key, data.state);
		}
		if (obj instanceof PlayerMouseMoved) {
			conn.getData().mouseLoc.set(((PlayerMouseMoved) obj).loc);
		}
	}

	public void ready() {
		lobby.playerReady(0);
		PlayerData.getInstance().ready = true;
		server.sendToAllTCP(new PlayerReady(0));
	}

	public void chat(String message) {
		lobby.chat(message);
		server.sendToAllTCP(new ChatMessage(message));
	}

	public void playerLeft(PlayerData data) {
		unused.add(data.id);
		Network.players.remove(data.id);
		lobby.playerLeft(data.id);
		server.sendToAllTCP(new PlayerLeft(data.id));
	}

	public void updateData() {
		server.sendToAllTCP(new ProcessTCP(Network.processTCP));
		server.sendToAllUDP(new ProcessUDP(Network.processUDP));
	}

	public Server getServer() {
		return server;
	}

	public void startGame() {
		server.sendToAllTCP(new GameStart());
		Global.game.transition(new GameScreen());

		for (PlayerData player : Network.players.values()) {
			Network.createActor(new BloodlineCreate(player.id));
		}
	}

}
