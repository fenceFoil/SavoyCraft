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
package com.savoycraft.opera;

import static com.esotericsoftware.minlog.Log.warn;

import java.util.LinkedList;

import org.w3c.dom.Element;

/**
 * 
 *
 */
public class BeatTrack {

	/**
	 * Unit: Seconds
	 */
	private LinkedList<Double> times = new LinkedList<Double>();

	public static BeatTrack load(Element beatElement) {
		BeatTrack t = new BeatTrack();

		String text = beatElement.getTextContent();
		String[] timeStrings = text.split(",");
		for (int i = 0; i < timeStrings.length; i++) {
			try {
				t.times.add(Double.parseDouble(timeStrings[i]));
			} catch (NumberFormatException e) {
				warn("BeatTrack", "Invalid number format for " + timeStrings[i]);
				e.printStackTrace();
			}
		}

		return t;
	}

	/**
	 * Returns shallow clone of the list
	 * 
	 * @return
	 */
	public LinkedList<Double> getTimes() {
		return (LinkedList<Double>) times.clone();
	}

	/**
	 * 
	 * @param min
	 *            inclusive
	 * @param max
	 *            exclusive
	 * @return never null
	 */
	public LinkedList<Double> getTimes(double min, double max) {
		LinkedList<Double> found = new LinkedList<Double>();

		for (Double d : times) {
			if (d >= min && d < max) {
				found.add(d);
			}
		}

		return found;
	}

}
