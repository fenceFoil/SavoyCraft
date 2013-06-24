/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SavoyCraft.
 * 
 * SavoyCraft is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SavoyCraft is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SavoyCraft. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.savoycraft.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 * 
 */
public class TimeUtil {

	private static boolean calibrated = false;

	private static long calibration = 0;

	private static long lastTimeRequest = 0;

	// List of time servers: http://tf.nist.gov/service/time-servers.html
	// Do not query time server more than once every 4 seconds
	public static final String TIME_SERVER = "time.nist.gov";

	/**
	 * Queries a NIST time server to get a difference between its time and the
	 * local system time.
	 */
	public static void calibrate() {
		if (System.currentTimeMillis() < lastTimeRequest + 5000 || calibrated) {
			return;
		}

		NTPUDPClient timeClient = new NTPUDPClient();
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(TIME_SERVER);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		TimeInfo timeInfo = null;
		try {
			timeInfo = timeClient.getTime(inetAddress);
		} catch (IOException e) {
			System.err.println("SavoyCraft could not contact time.nist.gov.");
			e.printStackTrace();
			return;
		}
		long returnTime = timeInfo.getReturnTime();
		calibration = System.currentTimeMillis() - returnTime;
		System.out.println(calibration);
		// Date time = new Date(returnTime);
		// System.out.println("Time from " + TIME_SERVER + ": " + time);

		lastTimeRequest = System.currentTimeMillis();

		calibrated = true;
	}

	public static long get() {
		if (!calibrated) {
			calibrate();
		}

		return System.currentTimeMillis() - calibration;
	}

	/**
	 * Just returns the system time in millis, formatted in hex
	 * 
	 * @return
	 */
	public static String getHexTime() {
		return Long.toHexString(System.currentTimeMillis());
	}
}
