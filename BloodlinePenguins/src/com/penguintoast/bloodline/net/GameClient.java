package com.penguintoast.bloodline.net;

import java.net.InetAddress;
import java.util.concurrent.Semaphore;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.listener.ClientListener;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;

public class GameClient {
	private Client client;
	private Semaphore lock;
	private Object received;
	
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
			client.connect(2000, addr, Network.TCP_PORT, Network.UDP_PORT);
			client.sendTCP(PlayerData.getInstance());
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public InfoResponse queryInfo(InetAddress addr) {
		try {
			client.connect(2000, addr, Network.TCP_PORT, Network.UDP_PORT);
			client.sendTCP(new InfoRequest());
			lock.acquire();
			client.close();
			return (InfoResponse) received;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void received(Object o) {
		received = o;
		lock.release();
	}

	public Client getClient() {
		return client;
	}

}
