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

import java.util.LinkedList;

import com.savoycraft.Skin;
import com.savoycraft.opera.Scene;
import com.savoycraft.update.ModVersion;

/**
 *
 */
public class Show {

	private String name;
	private String showID;
	private LinkedList<Skin> customSkins;
	private long created;
	private ModVersion modVersion;
	private LinkedList<ShowScene> scenes = new LinkedList<ShowScene>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowID() {
		return showID;
	}

	public void setShowID(String showID) {
		this.showID = showID;
	}

	public LinkedList<Skin> getCustomSkins() {
		return customSkins;
	}

	public void setCustomSkins(LinkedList<Skin> customSkins) {
		this.customSkins = customSkins;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public ModVersion getModVersion() {
		return modVersion;
	}

	public void setModVersion(ModVersion modVersion) {
		this.modVersion = modVersion;
	}

	public LinkedList<Scene> getScenes() {
		return scenes;
	}

	public void setScenes(LinkedList<Scene> scenes) {
		this.scenes = scenes;
	}
}
