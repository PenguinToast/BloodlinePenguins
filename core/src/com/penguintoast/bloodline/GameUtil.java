package com.penguintoast.bloodline;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class GameUtil {

	public static byte[] getHWID() {
		try {
			return NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
