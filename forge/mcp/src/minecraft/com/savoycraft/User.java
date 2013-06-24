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
package com.savoycraft;

import java.io.Serializable;

/**
 * Represents somebody who uses SavoyCraft in multiplayer.
 * 
 * @author BJ
 * 
 */
public class User implements Serializable {

	/**
	 * Minecraft user name
	 */
	private String username;
	/**
	 * Google Drive id of the user's SavoyCraft folder
	 */
	private String driveFolderID;

	/**
	 * @param username
	 * @param driveFolderID
	 */
	public User(String username, String driveFolderID) {
		super();
		this.username = username;
		this.driveFolderID = driveFolderID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDriveFolderID() {
		return driveFolderID;
	}

	public void setDriveFolderID(String driveFolderID) {
		this.driveFolderID = driveFolderID;
	}

}
