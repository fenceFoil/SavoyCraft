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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 *
 */
public class FileUtil {

	/**
	 * 
	 * @param dir
	 * @param regex
	 *            regex is of filename, not path
	 * @return a list; possibly empty, not null
	 */
	public static List<File> fileSearch(File dir, String regex) {
		LinkedList<File> found = new LinkedList<File>();

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				found.addAll(fileSearch(f, regex));
			} else {
				if (f.getName().matches(regex)) {
					found.add(f);
				}
			}
		}

		return found;
	}

	/**
	 * Searches for matching directories. If a match is found, it is not
	 * searched inside for more inner matches.
	 * 
	 * @param dir
	 * @param regex
	 * @return
	 */
	public static LinkedList<File> dirSearch(File dir, String regex) {
		LinkedList<File> found = new LinkedList<File>();

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				if (f.getName().matches(regex)) {
					found.add(f);
				} else {
					found.addAll(dirSearch(f, regex));
				}
			}
		}

		return found;
	}
}
