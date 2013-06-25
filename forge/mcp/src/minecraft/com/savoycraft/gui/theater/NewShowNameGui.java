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
package com.savoycraft.gui.theater;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.savoycraft.tempoGui.TGButton;
import com.savoycraft.tempoGui.TGFrame;
import com.savoycraft.tempoGui.TGListener;
import com.savoycraft.tempoGui.TGTextField;
import com.savoycraft.tempoGui.event.TGEvent;
import com.savoycraft.theater.Show;
import com.savoycraft.theater.Theater;

/**
 * @author BJ
 * 
 */
public class NewShowNameGui extends TGFrame {

	public NewShowNameGui(final GuiScreen backScreen, final Theater theater, final Show show) {
		super(backScreen, "Name the Show:");
		setFrameSize(200, 90);
		final TGTextField tf = new TGTextField(2, 20, getFrameWidth() - 4, 20);
		add(tf);
		add(new TGButton(2, 45, getFrameWidth() / 2 - 4, 20, "OK").addListener(new TGListener() {
			
			@Override
			public void onTGEvent(TGEvent event) {
				show.setName(tf.getText());
				theater.getShows().add(show);
				Minecraft.getMinecraft().displayGuiScreen(new ShowsGui(new TheaterGui(null, theater), theater));
			}
		}));
		add(new TGButton(2 + getFrameWidth()/2, 45, getFrameWidth() / 2 - 4, 20, "Cancel").addListener(new TGListener() {
			
			@Override
			public void onTGEvent(TGEvent event) {
				Minecraft.getMinecraft().displayGuiScreen(backScreen);
			}
		}));
	}
}
