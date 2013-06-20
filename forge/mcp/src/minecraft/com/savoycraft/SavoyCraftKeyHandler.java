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

import static com.esotericsoftware.minlog.Log.info;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.savoycraft.gui.TDEditCourseGui;
import com.savoycraft.gui.TGFrame;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

/**
 * 
 */
public class SavoyCraftKeyHandler extends KeyHandler {

	public SavoyCraftKeyHandler() {
		// the first value is an array of KeyBindings, the second is whether or
		// not the call
		// keyDown should repeat as long as the key is down
		super(new KeyBinding[] { new KeyBinding("SavoyCraft", Keyboard.KEY_L),
				new KeyBinding("GUITest", Keyboard.KEY_G) }, new boolean[] {
				false, false });
	}

	@Override
	public String getLabel() {
		return "SavoyCraft Key Handler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {
		// Minecraft.getMinecraft().displayGuiScreen(new TGFrame(null, 350,
		// 150));

		if (Minecraft.getMinecraft().currentScreen == null) {
			if (kb.keyDescription.equals("SavoyCraft")) {
				info("Key pressed for SavoyCraft!");
				SavoyCraft.getConductor().playScene("princessida:command");
			} else {
				// Minecraft.getMinecraft().displayGuiScreen(
				// new TGFrame(null, 200, 150));
				Minecraft.getMinecraft().displayGuiScreen(
						new TDEditCourseGui(null));
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		// TODO Auto-generated method stub

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
