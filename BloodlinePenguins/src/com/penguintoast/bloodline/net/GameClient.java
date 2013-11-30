package com.penguintoast.bloodline.net;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.screens.LobbyScreen;
import com.penguintoast.bloodline.net.listener.ClientListener;
import com.penguintoast.bloodline.net.objects.ChatMessage;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.net.objects.game.ProcessTCP;
import com.penguintoast.bloodline.net.objects.game.ProcessUDP;
import com.penguintoast.bloodline.net.objects.lobby.PlayerJoined;
import com.penguintoast.bloodline.net.objects.lobby.PlayerLeft;
import com.penguintoast.bloodline.net.objects.lobby.PlayerReady;

public class GameClient {
	private LobbyScreen lobby;

	private Client client;
	private Semaphore lock;
	private Object received;

	public void setLobby(LobbyScreen screen) {
		lobby = screen;
	}

	public void start() {
		try {
			Network.actorCount = 0;
			lock = new Semaphore(0);

			client = new Client();

			Network.register(client);

			client.addListener(new ThreadedListener(new ClientListener(this)));

			client.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void shutdown() {
		Network.players.clear();
		Network.actors.clear();
		client.close();
	}

	public JoinResponse joinServer(InetAddress addr) {
		try {
			synchronized (client) {
				client.connect(2000, addr, Network.TCP_PORT, Network.UDP_PORT);
				client.sendTCP(PlayerData.getInstance());
				lock.acquire();
				return (JoinResponse) received;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public InfoResponse queryInfo(InetAddress addr) {
		try {
			synchronized (client) {
				if (client.isConnected()) {
					return null;
				}
				client.connect(2000, addr, Network.TCP_PORT, Network.UDP_PORT);
				client.sendTCP(new InfoRequest());
				lock.acquire();
				client.close();
				return (InfoResponse) received;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void received(Object o) {
		received = o;
		lock.release();
		if (o instanceof ProcessTCP) {
			Network.processTCP = (LinkedList<Object>) o;
		}
		if (o instanceof ProcessUDP) {
			Network.processUDP = (LinkedList<Object>) o;
		}
		if (o instanceof PlayerJoined) {
			PlayerData dat = ((PlayerJoined) o).player;
			lobby.playerJoined(dat);
			Network.players.put(dat.id, dat);
		}
		if (o instanceof PlayerLeft) {
			int id = ((PlayerLeft) o).id;
			lobby.playerLeft(id);
			Network.players.remove(id);
		}
		if (o instanceof JoinResponse) {
			JoinResponse dat = (JoinResponse) o;
			if(dat.response == JoinResponse.ACCEPTED) {
				PlayerData.getInstance().id = dat.pID;
				for(PlayerData pl : dat.players) {
					Network.players.put(pl.id, pl);
				}
			}
		}
		if (o instanceof ChatMessage) {
			lobby.chat(((ChatMessage) o).message);
		}
		if (o instanceof PlayerReady) {
			int id = ((PlayerReady) o).id;
			Network.players.get(id).ready = true;
			lobby.playerReady(id);
		}
	}
	
	public void ready() {
		client.sendTCP(new PlayerReady());
	}
	
	public void chat(String message) {
		client.sendTCP(new ChatMessage(message));
	}
	
	public void disconnected() {
		if(lobby != null) {
			lobby.showErrorDialog();
		}
	}

	public Client getClient() {
		return client;
	}

}
