package com.penguintoast.bloodline.net;

import java.net.InetAddress;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.listener.ClientListener;
import com.penguintoast.bloodline.net.objects.ChatMessage;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.net.objects.game.ProcessTCP;
import com.penguintoast.bloodline.net.objects.game.ProcessUDP;
import com.penguintoast.bloodline.net.objects.lobby.GameStart;
import com.penguintoast.bloodline.net.objects.lobby.PlayerJoined;
import com.penguintoast.bloodline.net.objects.lobby.PlayerLeft;
import com.penguintoast.bloodline.net.objects.lobby.PlayerReady;
import com.penguintoast.bloodline.ui.screens.GameScreen;
import com.penguintoast.bloodline.ui.screens.LobbyScreen;

public class GameClient {
	private LobbyScreen lobby;
	private GameScreen game;

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

	public void received(final Object o) {
		received = o;
		if (lock.availablePermits() <= 0) {
			lock.release();
		}
		if (o instanceof ProcessTCP) {
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					Network.processTCP.push(((ProcessTCP) o).objects);
				}
			});
		}
		if (o instanceof ProcessUDP) {
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					Network.processUDP = ((ProcessUDP) o).objects;
				}
			});
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
			if (dat.response == JoinResponse.ACCEPTED) {
				PlayerData.getInstance().id = dat.pID;
				for (PlayerData pl : dat.players) {
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
		if (o instanceof GameStart) {
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					game = new GameScreen();
					Global.game.transition(game);
				}
			});
		}
	}

	public void ready() {
		client.sendTCP(new PlayerReady());
	}

	public void chat(String message) {
		client.sendTCP(new ChatMessage(message));
	}

	public void disconnected() {
		if (lobby != null) {
			lobby.showErrorDialog();
		}
		if (game != null) {
			game.showErrorDialog();
		}
	}

	public Client getClient() {
		return client;
	}

}
