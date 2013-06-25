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

import com.savoycraft.User;
import com.savoycraft.opera.OperaScene;
import com.savoycraft.update.ModVersion;

/**
 *
 */
public class ShowScene {
	private String sceneID;
	private ModVersion sceneVersion;
	private LinkedList<User> cast;

	public ShowScene(OperaScene scene) {
		setSceneID(scene.getHandle());
	}

	public String getSceneID() {
		return sceneID;
	}

	public void setSceneID(String sceneID) {
		this.sceneID = sceneID;
	}

	public ModVersion getSceneVersion() {
		return sceneVersion;
	}

	public void setSceneVersion(ModVersion sceneVersion) {
		this.sceneVersion = sceneVersion;
	}

	public LinkedList<User> getCast() {
		return cast;
	}

	public void setCast(LinkedList<User> cast) {
		this.cast = cast;
	}

}
