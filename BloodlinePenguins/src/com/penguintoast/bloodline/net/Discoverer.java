package com.penguintoast.bloodline.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost;
import com.esotericsoftware.kryonet.Serialization;

public class Discoverer {
	private static void broadcast (int udpPort, DatagramSocket socket, Serialization serialization) throws IOException {
		ByteBuffer dataBuffer = ByteBuffer.allocate(64);
		serialization.write(null, dataBuffer, new DiscoverHost());
		dataBuffer.flip();
		byte[] data = new byte[dataBuffer.limit()];
		dataBuffer.get(data);
		for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			for (InetAddress address : Collections.list(iface.getInetAddresses())) {
				byte[] ip = address.getAddress();
				ip[3] = -1; // 255.255.255.0
				try {
					socket.send(new DatagramPacket(data, data.length, InetAddress.getByAddress(ip), udpPort));
				} catch (Exception ignored) {
				}
				ip[2] = -1; // 255.255.0.0
				try {
					socket.send(new DatagramPacket(data, data.length, InetAddress.getByAddress(ip), udpPort));
				} catch (Exception ignored) {
				}
				ip[1] = -1; // 255.0.0.0
				try {
					socket.send(new DatagramPacket(data, data.length, InetAddress.getByAddress(ip), udpPort));
				} catch (Exception ignored) {
				}
			}
		}
	}

	/** Broadcasts a UDP message on the LAN to discover any running servers.
	 * @param udpPort The UDP port of the server.
	 * @param timeoutMillis The number of milliseconds to wait for a response. */
	public static void discoverHosts (int udpPort, int timeoutMillis, Serialization ser, DiscoverListener listener) {
		Thread t = new Thread(new DiscovererRunner(udpPort, timeoutMillis, ser, listener));
		t.start();
	}

	private static class DiscovererRunner implements Runnable {
		private int udpPort, timeoutMillis;
		private Serialization ser;
		private DiscoverListener listener;
		
		public DiscovererRunner(int udpPort, int timeoutMillis, Serialization ser, DiscoverListener listener) {
			this.udpPort = udpPort;
			this.timeoutMillis = timeoutMillis;
			this.ser = ser;
			this.listener = listener;
		}
		@Override
		public void run() {
			long start = System.currentTimeMillis();
			while(System.currentTimeMillis() - start < timeoutMillis) {
				try {
					listener.hostDiscovered(InetAddress.getLocalHost());
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*DatagramSocket socket = null;
			try {
				socket = new DatagramSocket();
				socket.setBroadcast(true);
				broadcast(udpPort, socket, ser);
				socket.setSoTimeout(timeoutMillis);
				while (true) {
					DatagramPacket packet = new DatagramPacket(new byte[0], 0);
					try {
						socket.receive(packet);
					} catch (SocketTimeoutException ex) {
					}
					listener.hostDiscovered(packet.getAddress());
				}
			} catch (IOException ex) {
			} finally {
				if (socket != null) socket.close();
			}*/
		}
	}
	
	public static interface DiscoverListener {
		void hostDiscovered(InetAddress addr);
	}
}
