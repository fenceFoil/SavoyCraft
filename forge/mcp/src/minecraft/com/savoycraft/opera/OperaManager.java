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

import static com.esotericsoftware.minlog.Log.info;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.savoycraft.TDConfig;
import com.savoycraft.util.FileUtil;

/**
 * 
 *
 */
public class OperaManager {
	private static HashMap<String, Opera> operas = new HashMap<String, Opera>();

	/**
	 * Initializes the list of operas, loading them from zip files in any
	 * location in the minecraft dir. May block for some seconds.
	 */
	public static void loadOperas() {
		operas.clear();

		List<File> foundOperas = FileUtil.dirSearch(TDConfig.getTDDir(),
				"opera.+");

		for (File f : foundOperas) {
			Opera opera = Opera.load(f);
			if (opera != null) {
				operas.put(opera.getHandle(), opera);
			}
		}

		info("Loaded " + operas.size() + " operas.");
	}

	/**
	 * Returns the given opera, or null
	 * 
	 * @param handle
	 * @return
	 */
	public static Opera getOpera(String handle) {
		return operas.get(handle);
	}

	public static HashMap<String, Opera> getOperas() {
		return operas;
	}

	public static LinkedList<String> getOperaTitles() {
		LinkedList<String> titles = new LinkedList<String>();
		for (Opera o : operas.values()) {
			titles.add(o.getName());
		}
		return titles;
	}

	public static LinkedList<Opera> getOperaList() {
		LinkedList<Opera> operas = new LinkedList<Opera>();
		operas.addAll(OperaManager.operas.values());
		return operas;
	}

	public static OperaScene getScene(String handle) {
		String[] parts = handle.split(":");
		Opera opera = getOpera(parts[0]);
		return opera.getScene(handle);
	}

}