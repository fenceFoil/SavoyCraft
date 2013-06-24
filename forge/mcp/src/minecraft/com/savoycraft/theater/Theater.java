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
package com.savoycraft.theater;

import java.util.HashSet;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;

import com.savoycraft.Rand;
import com.savoycraft.User;
import com.savoycraft.update.ModVersion;

/**
 * 
 *
 */
public class Theater {

	private String name = "";
	private String theaterID;
	private long created;
	private User owner;
	private ModVersion modVersion;

	private HashSet<User> troupe = new HashSet<User>();

	private LinkedList<Show> shows = new LinkedList<Show>();

	/**
	 * @param theatreCode
	 * 
	 */
	public Theater(String theatreCode) {
		setTheaterID(theatreCode);
		setName(generateName());
	}

	private static final String[] prefixes = { "The", "Blue", "Grande",
			"Grand", "Star", "The New" };
	private static final String[] suffixes = { "Theatre", "Theater",
			"Opera House", "Opera", "Stage", "Centre" };

	public static String generateName() {
		StringBuilder name = new StringBuilder();
		boolean forceSuffix = false;
		if (Rand.om.nextBoolean()) {
			name.append(prefixes[Rand.om.nextInt(prefixes.length)]).append(" ");
			forceSuffix = true;
		}
		name.append(Minecraft.getMinecraft().thePlayer.getEntityName());
		if (Rand.om.nextBoolean() || forceSuffix) {
			name.append(" ").append(suffixes[Rand.om.nextInt(suffixes.length)]);
		}
		return name.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTheaterID() {
		return theaterID;
	}

	public void setTheaterID(String theaterID) {
		this.theaterID = theaterID;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public ModVersion getModVersion() {
		return modVersion;
	}

	public void setModVersion(ModVersion modVersion) {
		this.modVersion = modVersion;
	}

	public HashSet<User> getTroupe() {
		return troupe;
	}

	public void setTroupe(HashSet<User> troupe) {
		this.troupe = troupe;
	}

	public LinkedList<Show> getShows() {
		return shows;
	}

	public void setShows(LinkedList<Show> shows) {
		this.shows = shows;
	}

}
