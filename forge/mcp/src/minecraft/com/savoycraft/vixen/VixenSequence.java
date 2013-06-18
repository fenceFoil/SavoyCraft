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
package com.savoycraft.vixen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.savoycraft.base64.Base64;
import com.savoycraft.util.DOMUtil;

/**
 * Represents a ".vix" file from Vixen version 2.1.1.* , NOT Vixen 3+.
 * 
 */
public class VixenSequence {
	private int eventPeriodMs;
	private int lengthMs;
	private LinkedList<VixenChannel> channels = new LinkedList<VixenChannel>();

	public VixenSequence(File file) throws IOException {
		// Load sequence XML
		Element root = DOMUtil.getRootElement(DOMUtil.loadDocument(file));
		NodeList rootNodes = root.getChildNodes();

		// Load basic properties of sequence
		eventPeriodMs = DOMUtil.parseInt(DOMUtil.findElement(
				"EventPeriodInMilliseconds", rootNodes).getTextContent());
		lengthMs = DOMUtil.parseInt(DOMUtil.findElement("Time", rootNodes)
				.getTextContent());

		// Load basic properties of channels
		Element channelsElement = DOMUtil.findElement("Channels", rootNodes);
		NodeList channelsNodes = channelsElement.getChildNodes();

		for (Element e : DOMUtil.findElements("Channel", channelsNodes)) {
			VixenChannel channel = new VixenChannel(DOMUtil.parseBoolean(e
					.getAttribute("enabled")), e.getTextContent(),
					DOMUtil.parseInt(e.getAttribute("color")));
			channels.add(channel);
		}

		// Load event data
		String eventDataString = DOMUtil.findElement("EventValues", rootNodes)
				.getTextContent();
		// Decode the base64, into a SIGNED byte array
		byte[] eventDataRaw = Base64.decode(eventDataString);
		// Copy the raw data as UNSIGNED bytes into an int array
		int[] eventData = new int[eventDataRaw.length];
		for (int i = 0; i < eventDataRaw.length; i++) {
			// Converts to unsigned value
			eventData[i] = eventDataRaw[i] & 0xff;
		}

		// Copy the event data into channels
		int eventPeriodCount = eventData.length / channels.size();
		// Set up the vixen channels to hold all the read events
		for (VixenChannel c : channels) {
			c.setEvents(new int[eventPeriodCount]);
		}

		// Read in the events
		for (int i = 0; i < eventPeriodCount; i++) {
			for (int channel = 0; channel < channels.size(); channel++) {
				VixenChannel currChannel = channels.get(channel);
				currChannel.getEvents()[i] = eventData[channel
						* eventPeriodCount + i];
			}
		}
	}

	public int getEventPeriodMs() {
		return eventPeriodMs;
	}

	public int getLengthMs() {
		return lengthMs;
	}

	public int eventPeriodToMs(int eventPeriod) {
		return eventPeriod * eventPeriodMs;
	}

	/**
	 * Uses eventPeriodMs to convert a list of event periods from VixenChannel
	 * to millesecond times.
	 * 
	 * @param periods
	 * @return
	 */
	public LinkedList<Integer> convertEventPeriodsToMs(
			LinkedList<Integer> periods) {
		for (int i = 0; i < periods.size(); i++) {
			periods.set(i, eventPeriodToMs(periods.get(i)));
		}
		return periods;
	}

	public LinkedList<VixenChannel> getChannels() {
		return channels;
	}

	public VixenChannel getChannel(int i) {
		if (i >= 0 && i < channels.size()) {
			return channels.get(i);
		} else {
			return null;
		}
	}
}
