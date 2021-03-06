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
package com.savoycraft.keyboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumOS;

import org.lwjgl.input.Keyboard;

import com.savoycraft.TDConfig;
import com.savoycraft.gui.MinetunesGui;
import com.savoycraft.gui.TDGui;

public class KeypressManager {

	private LinkedList<KeyBinding> bindings = (LinkedList<KeyBinding>) DEFAULT_BINDINGS
			.clone();

	private File configFile = new File(TDConfig.getTDDir(), "keys.txt");

	private static final LinkedList<KeyBinding> DEFAULT_BINDINGS = new LinkedList<KeyBinding>();
	public static final int TUNE_NOTEBLOCK_KEY_UNSET = 234233;
	static {
		// Set up default key bindings
//		KeyBinding muteBinding = new KeyBinding("Mute", "mute", Keyboard.KEY_M,
//				KeyBinding.CTRL_KEYS);
//		DEFAULT_BINDINGS.add(muteBinding);
//
		KeyBinding menuBinding = new KeyBinding("SavoyCraft", "menu",
				Keyboard.KEY_P, KeyBinding.CTRL_KEYS);
		DEFAULT_BINDINGS.add(menuBinding);
//
//		KeyBinding showErrorsBinding = new KeyBinding("Hide SignTune Errors",
//				"showErrors", Keyboard.KEY_S, KeyBinding.CTRL_KEYS);
//		DEFAULT_BINDINGS.add(showErrorsBinding);
//
//		KeyBinding mcdittyOffBinding = new KeyBinding("Turn Off MineTunes",
//				"mcdittyOff", Keyboard.KEY_O, KeyBinding.CTRL_KEYS);
//		DEFAULT_BINDINGS.add(mcdittyOffBinding);
//
//		KeyBinding playBookBinding = new KeyBinding("Play Held Book",
//				"playBook", Keyboard.KEY_B, KeyBinding.CTRL_KEYS);
//		DEFAULT_BINDINGS.add(playBookBinding);
//
//		KeyBinding tuneNoteblockBinding = new KeyBinding("Tune Noteblock",
//				"tuneNoteblock", TUNE_NOTEBLOCK_KEY_UNSET,
//				KeyBinding.SHIFT_KEYS);
//		DEFAULT_BINDINGS.add(tuneNoteblockBinding);

		// KeyBinding inspireBookBinding = new KeyBinding(
		// "Improvise Ditty From Book", "inspireBook", Keyboard.KEY_I,
		// KeyBinding.CTRL_KEYS);
		// DEFAULT_BINDINGS.add(inspireBookBinding);
	}

	public void update() {
		for (KeyBinding b : bindings) {
			// Since the Minecraft gamesetttings can't be read when the static{}
			// block is called, they are used here instead in setting up the
			// tune noteblock key
			if (b.getMainKey() == TUNE_NOTEBLOCK_KEY_UNSET) {
				b.setMainKey(Minecraft.getMinecraft().gameSettings.keyBindForward.keyCode);
				writeConfig();
			}

			if (b.isLastState()) {
				// Wait for release
				if (!Keyboard.isKeyDown(b.getMainKey())) {
					b.setLastState(false);
				}
			} else {
				// Check all keys in the key binding
				// Shift as used here could mean ctrl as well.
				boolean isAShiftDown = false;
				int[] shiftKeys = b.getModifierKeys();
				if (shiftKeys != null && shiftKeys.length > 0) {
					for (int i : shiftKeys) {
						if (Keyboard.isKeyDown(i)) {
							// Shift key is pressed, binding could be activated
							isAShiftDown = true;
							break;
						} else {
							// Not pressed
							if (Minecraft.getOs() == EnumOS.MACOS) {
								// If checking for a ctrl key, automatically
								// check
								// for the mac meta key as well
								if (i == 29) {
									if (Keyboard.isKeyDown(219)) {
										// Mac meta key is pressed
										isAShiftDown = true;
										break;
									}
								} else if (i == 157) {
									if (Keyboard.isKeyDown(220)) {
										// Mac meta key is pressed
										isAShiftDown = true;
										break;
									}
								}
							}
						}
					}
				} else {
					// No shift keys; always count them as pressed
					isAShiftDown = true;
				}

				// if shifts are sorted out, check for main key
				if (isAShiftDown) {
					if (Keyboard.isKeyDown(b.getMainKey())) {
						// Activated!
						executeAction(b.getAction());
						// Note activation in last state
						b.setLastState(true);
					}
				} else {
					// No shifts, no activation
				}
			}
		}
	}

	/**
	 * Executes some result determined by the action string of a keybinding.
	 * 
	 * @param action
	 */
	private void executeAction(String action) {
		if (action.equalsIgnoreCase("menu")) {
			// Show menu
			TDGui guiToOpen = new TDGui(null);
			Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
		}
	}

	public void addBinding(KeyBinding binding) {
		bindings.add(binding);
	}

	/**
	 * Loads key configurations from a file if possible, and if the the file
	 * does not exist it resets this Processor to defaults.
	 * 
	 * The config file can have comments on a given line by starting it with #
	 */
	public void loadConfig() {
		// Reset to default key bindings
		resetToDefaults();

		// Try to load config file
		// For each key binding in the config file, overwrite the key of the
		// equivalent default binding
		if (configFile.exists() && configFile.canRead()) {
			try {
				BufferedReader configReader = new BufferedReader(
						new FileReader(configFile));

				while (true) {
					String inLine = configReader.readLine();
					if (inLine == null) {
						break;
					} else if (inLine.startsWith("#")) {
						continue;
					} else {
						// Read in a key binding
						KeyBinding readBinding = KeyBinding
								.fromConfigString(inLine);
						// Assign it's keys to the matching default binding
						for (int i = 0; i < bindings.size(); i++) {
							if (bindings.get(i).getAction()
									.equalsIgnoreCase(readBinding.getAction())) {
								// Matching binding found
								bindings.get(i).setMainKey(
										readBinding.getMainKey());
								bindings.get(i).setModifierKeys(
										readBinding.getModifierKeys());
							}
						}
					}
				}

				configReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// System.out.println("No MineTunes Key Bindings File Found");
		}
	}

	public void writeConfig() {
		try {
			configFile.delete();
			configFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// Write config
		BufferedWriter configWriter;
		try {
			configWriter = new BufferedWriter(new FileWriter(configFile));
			configWriter
					.write("# SavoyCraft Key Settings: Editing this is not advised. "
							+ "To reset to defaults, delete this file.");
			for (KeyBinding b : bindings) {
				configWriter.newLine();
				configWriter.write(b.toConfigString());
			}
			configWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Provided for MineTunesConfig, when it loads old config files that specify
	 * the Mute key ID.
	 * 
	 * @param parseInt
	 */
	public void setMuteKeyID(int parseInt) {
		for (KeyBinding b : bindings) {
			if (b.getAction().equals("mute")) {
				b.setMainKey(parseInt);
			}
		}
	}

	/**
	 * Provided for MineTunesConfig, when it loads old config files that specify
	 * the Menu key ID.
	 * 
	 * @param parseInt
	 */
	public void setGuiKeyID(int parseInt) {
		for (KeyBinding b : bindings) {
			if (b.getAction().equals("menu")) {
				b.setMainKey(parseInt);
			}
		}
	}

	/**
	 * Tries to return a keybinding with the action listed from this processor's
	 * list of bindings.
	 * 
	 * @param action
	 * @return null if none found
	 */
	public KeyBinding getBindingByAction(String action) {
		KeyBinding returnBinding = null;
		for (KeyBinding b : bindings) {
			if (b.getAction().equalsIgnoreCase(action)) {
				returnBinding = b;
				break;
			}
		}
		return returnBinding;
	}

	public LinkedList<KeyBinding> getBindings() {
		return bindings;
	}

	public void resetToDefaults() {
		bindings.clear();
		for (int i = 0; i < DEFAULT_BINDINGS.size(); i++) {
			try {
				bindings.add((KeyBinding) DEFAULT_BINDINGS.get(i).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
}
