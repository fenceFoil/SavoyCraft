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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 
 */
public class ZipUtil {

	/**
	 * Copies the files in a zip file to the given directory in the filesystem.
	 * 
	 * @param zipFileName
	 * @param destName
	 */
	public static void extractZipFiles(String zipFileName, String destName) {
		try {
			byte[] buf = new byte[1024];
			ZipInputStream zipInputStream = null;
			ZipEntry currZipEntry;
			zipInputStream = new ZipInputStream(
					new FileInputStream(zipFileName));
			currZipEntry = zipInputStream.getNextEntry();

			while (currZipEntry != null) {
				// for each entry to be extracted
				String entryName = currZipEntry.getName();
				// System.out.println("Extracting MineTunes ZIP Entry: "
				// + entryName);

				File newFile = new File(destName + File.separator + entryName);
				if (currZipEntry.isDirectory()) {
					newFile.mkdirs();
				} else {
					newFile.getParentFile().mkdirs();
					FileOutputStream fileOutputStream = new FileOutputStream(
							newFile);
					int n;
					while ((n = zipInputStream.read(buf, 0, 1024)) > -1)
						fileOutputStream.write(buf, 0, n);

					fileOutputStream.close();
				}

				zipInputStream.closeEntry();
				currZipEntry = zipInputStream.getNextEntry();
			}

			zipInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extracts and returns a single entry from a zip file. Cannot be a
	 * directory in the zip. Case sensitive.
	 * 
	 * @param zipFileName
	 * @param entryName
	 * @return null if file not read or found, possibly 0 elements
	 */
	public static byte[] extract(String zipFileName, String entryName) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream(
					new FileInputStream(zipFileName));
			ZipEntry currZipEntry = zipInputStream.getNextEntry();

			while (currZipEntry != null) {
				// for each entry to be extracted
				String currEntryName = currZipEntry.getName();

				if (!currZipEntry.isDirectory() && currEntryName.equals(entryName)) {
					int entrySize = (int) currZipEntry.getSize();
					byte[] buffer = new byte[entrySize];
					int readBytes = zipInputStream.read(buffer, 0, buffer.length);
					// Do not return if the read data is less than expected
					if (readBytes < buffer.length) {
						zipInputStream.close();
						return null;
					} else {
						zipInputStream.close();
						return buffer;
					}
					
				}

				zipInputStream.closeEntry();
				currZipEntry = zipInputStream.getNextEntry();
			}

			zipInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
}
