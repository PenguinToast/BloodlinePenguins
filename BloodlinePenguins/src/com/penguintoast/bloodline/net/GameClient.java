package com.penguintoast.bloodline.net;

import java.net.InetAddress;
import java.util.concurrent.Semaphore;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.gui.screens.LobbyScreen;
import com.penguintoast.bloodline.net.listener.ClientListener;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.PlayerJoined;
import com.penguintoast.bloodline.net.objects.PlayerLeft;

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
			lock = new Semaphore(0);

			client = new Client();

			Network.register(client);

			client.addListener(new ThreadedListener(new ClientListener(this)));

			client.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean joinServer(InetAddress addr) {
		try {
			synchronized (client) {
				client.connect(2000, addr, Network.TCP_PORT, Network.UDP_PORT);
				client.sendTCP(PlayerData.getInstance());
			}
			return true;
		} catch (Exception e) {
			return false;
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
				if (received instanceof InfoResponse) {
					return (InfoResponse) received;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void received(Object o) {
		received = o;
		lock.release();
		if (o instanceof PlayerData) {
			PlayerData.updateInstance((PlayerData) o);
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
	}

	public Client getClient() {
		return client;
	}

}
