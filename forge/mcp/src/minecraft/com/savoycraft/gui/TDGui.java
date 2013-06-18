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
package com.savoycraft.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author BJ
 * 
 */
public class TDGui extends TGFrame {

	/**
	 * @param backScreen
	 */
	public TDGui(GuiScreen backScreen) {
		super(backScreen);
		setFrameSize(200, 105);

		add(new TGButton(25, 5, 150, 20, "New Course")
				.addListener(new TGListener() {

					@Override
					public void onTGEvent(TGEvent event) {
						mc.displayGuiScreen(new TDEditCourseGui(null));
					}
				}));
		add(new TGButton(25, 30, 150, 20, "Edit Course"));
		add(new TGButton(25, 55, 150, 20, "Settings"));
		add(new TGButton(25, 80, 150, 20, "Update"));
	}

}
