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

import java.util.LinkedList;

/**
 * Represents a channel's events and properties in a vixen sequence. To use, set
 * the properties in the constructor, then use setEvents to initialize this
 * object with an array with as many elements as there are event periods in the
 * sequence.
 * 
 */
public class VixenChannel {
	private boolean enabled;
	private String name;
	private int color;
	private int[] events;
	private LinkedList<Integer> eventList;

	public VixenChannel(boolean enabled, String name, int color) {
		super();
		this.enabled = enabled;
		this.name = name;
		this.color = color;
	}

	public int[] getEvents() {
		return events;
	}

	public void setEvents(int[] events) {
		this.events = events;
	}

	/**
	 * The event list simply lists times (in event periods) that the eventList
	 * values are equal to 255.
	 * 
	 * @return
	 */
	public LinkedList<Integer> getEventList() {
		if (eventList == null) {
			// Populate the list
			eventList = new LinkedList<Integer>();
			for (int i = 0; i < events.length; i++) {
				if (events[i] == 255) {
					eventList.add(i);
				}
			}
		}
		return (LinkedList<Integer>) eventList.clone();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
