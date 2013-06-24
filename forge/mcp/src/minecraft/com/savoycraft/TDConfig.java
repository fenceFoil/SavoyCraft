/**
 * Copyright (c) 2012 William Karnavas 
 * All Rights Reserved
 */

/**
 * 
 * This file is part of MineTunes.
 * 
 * MineTunes is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * MineTunes is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MineTunes. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.savoycraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import net.minecraft.client.Minecraft;

import com.savoycraft.resources.UpdateResourcesThread;
import com.savoycraft.update.CompareVersion;
import com.savoycraft.update.ModVersion;

/**
 * Represents the values stored in the MineTunes config file and other
 * information.
 * 
 */
public class TDConfig {

	/**
	 * Current mod version.
	 */
	@Deprecated
	public static final String CURRENT_VERSION = ModVersion.CURRENT_VERSION
			.toString();
	/**
	 * Minecraft version that the mod is designed for.
	 */
	public static final String MC_CURRENT_VERSION = "1.5.2";
	private static final String[] UPDATE_MESSAGE = {};
	// /**
	// * Disable to stop costly printlns from being called
	// */
	// public static boolean DEBUG = false;

	// /**
	// * Used when Auto-Updating. A rough list of packages to remove when
	// * stripping out this version of Minetunes.
	// */
	// public static final String[] mineTunesModPackages = { "org/jfugue",
	// "com/minetunes", "aurelienribon/tweenengine", "de/jarnbjo" };

	/**
	 * Whether config file is loaded yet
	 */
	private static boolean configLoaded = false;

	private static Properties defaultProperties = new Properties();
	static {
		// SET UP DEFAULT PROPERTIES, if normal defaults for primitive type are
		// unsuitable

	}

	/**
	 * Holds most config settings. accessed by various
	 * "getInt, getString, getBoolean, ..." methods in this class.
	 */
	private static Properties properties = new Properties(defaultProperties);

	/**
	 * The properties file
	 */
	private static File propertiesFile = new File(getTDDir().getPath()
			+ File.separator + "settings.xml");
	private static File resourcesDir = new File(getTDDir().getPath()
			+ File.separator + "resources");

	/**
	 * @return
	 */
	public static File getTDDir() {
		return new File(Minecraft.getMinecraftDir().getPath() + File.separator
				+ "savoyCraft" + File.separator);
	}

	/**
	 * @return
	 */
	public static File getResourcesDir() {
		return resourcesDir;
	}

	/**
	 * Checks whether the config file has been loaded. If not, this loads it. If
	 * the config file is outdated, this updates it and loads it.
	 * 
	 * Also loads noPlayTokens if the config isn't loaded yet.
	 * 
	 * @param world
	 *            needed to display chat message to player
	 */
	public static void loadAndUpdateSettings() {
		// Check that config file is loaded, and load it if it isn't
		if (!configLoaded) {
			// Load config settings
			load();

			int versionCompare = CompareVersion.compareVersions(
					CURRENT_VERSION,
					getStringWithDefault("mod.lastVersionRun", "0"));

			if (versionCompare == CompareVersion.GREATER) {
				// Config is old
				// Write a new one that is up to date
				try {
					flush();
					SavoyCraft.chat("§aSavoyCraft updated to version "
							+ CURRENT_VERSION + "!");
					if (UPDATE_MESSAGE != null) {
						for (String s : UPDATE_MESSAGE) {
							SavoyCraft.chat(s);
						}
					}
					// Update resources
					updateResources();
				} catch (IOException e) {
					// TODO Tell user
					e.printStackTrace();
				}
			} else if (versionCompare == CompareVersion.LESSER) {
				// Gulp! Config is too new!
				SavoyCraft.chat("§eSavoyCraft reverted to version "
						+ CURRENT_VERSION + " from "
						+ getString("mod.lastVersionRun")
						+ ". These are the good old days.");
				if (UPDATE_MESSAGE != null) {
					for (String s : UPDATE_MESSAGE) {
						SavoyCraft.chat(s);
					}
				}

				try {
					flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Just in case MC version changed too
				updateResources();
			} else {
				// If up to date, and resources have failed to load during the
				// last update, download resources now
				if (getBoolean("resources.missing")) {
					updateResources();
				}
			}
		}
	}

	/**
	 * Launches a thread to check for and update mod resources as necessary
	 */
	public static void updateResources() {
		new UpdateResourcesThread().start();
	}

	/**
	 * Thread-safe.
	 * 
	 * @throws IOException
	 */
	public static void flush() throws IOException {
		// NOT Properties (already used as lock in flushPropertiesXML)
		synchronized (propertiesFile) {
			// Create MineTunes dir if it does not exist already
			propertiesFile.getParentFile().mkdirs();

			// Save keypress handler settings
			SavoyCraft.keys.writeConfig();

			// Finally, note the current version of the mod before saving
			setString("mod.lastVersionRun", CURRENT_VERSION);

			// Flush properties
			flushProperties();
		}
	}

	/**
	 * Loads values in config file, if it exists, to various static variables in
	 * BlockSign. If file does not exist, creates a new one. If config was
	 * outdated (but probably loaded anyway), return false.
	 * 
	 * @return true if config file is up to date, false if obsolete
	 */
	private static void load() {
		// Load keyboard settings
		SavoyCraft.keys.loadConfig();

		// Load Properties
		try {
			FileInputStream xmlIn = new FileInputStream(propertiesFile);
			properties.loadFromXML(xmlIn);
			xmlIn.close();
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		} catch (InvalidPropertiesFormatException e1) {
			// e1.printStackTrace();
		} catch (IOException e1) {
			// e1.printStackTrace();
		}

		// Note that the config was loaded
		configLoaded = true;

		return;
	}

	private static void flushProperties() {
		// NOT PropertiesFile (used as lock in flushAll)
		synchronized (properties) {
			// Save XML settings
			try {
				propertiesFile.getParentFile().mkdirs();
				propertiesFile.delete();
				propertiesFile.createNewFile();
				FileOutputStream xmlOut = new FileOutputStream(propertiesFile);
				properties.storeToXML(xmlOut,
						"MineTunes Mod Settings: Version " + CURRENT_VERSION);
				xmlOut.flush();
				xmlOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isModOff() {
		return getBoolean("mod.off");
	}

	/**
	 * Flush still rqd after call
	 * 
	 * @param off
	 */
	public static void setModOff(boolean off) {
		setBoolean("mod.off", off);
	}

	public static boolean getBoolean(String key) {
		String value = properties.getProperty(key, "false");
		boolean boolValue = false;
		try {
			boolValue = Boolean.parseBoolean(value);
		} catch (Exception e) {
		}
		return boolValue;
	}

	/**
	 * Handles setting particlesEnabled and noteParticlesDisabled as fields in
	 * this class as well
	 * 
	 * @param key
	 * @param value
	 */
	public static void setBoolean(String key, boolean value) {
		properties.setProperty(key, Boolean.toString(value));
	}

	/**
	 * 
	 * @param key
	 *            value or null if none is defined
	 * @return
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return value or default if none is defined
	 */
	public static String getStringWithDefault(String key, String defaultValue) {
		String value = getString(key);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public static void setString(String key, String value) {
		properties.setProperty(key, value);
	}

	public static void setInt(String key, int value) {
		properties.setProperty(key, Integer.toString(value));
	}

	/**
	 * 
	 * @param key
	 * @return 0 if no key was defined or there is a formatting error
	 */
	public static int getInt(String key) {
		String value = properties.getProperty(key, "0");
		int keyValue = 0;
		try {
			keyValue = Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
		return keyValue;
	}

	public static void setFloat(String key, float value) {
		properties.setProperty(key, Float.toString(value));
	}

	/**
	 * 
	 * @param key
	 * @return 0 is default for malformed float or missing key
	 */
	public static float getFloat(String key) {
		String value = properties.getProperty(key, "0");
		float keyValue = 0;
		try {
			keyValue = Float.parseFloat(value);
		} catch (Exception e) {
			return 0;
		}
		return keyValue;
	}

	/**
	 * Toggles the boolean value of the given key.
	 * 
	 * @param key
	 * @return the new value
	 */
	public static boolean toggleBoolean(String key) {
		boolean currValue = getBoolean(key);
		setBoolean(key, !currValue);
		return !currValue;
	}
}
