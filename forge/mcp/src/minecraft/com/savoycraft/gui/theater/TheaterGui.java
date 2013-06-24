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
import com.savoycraft.tempoGui.TGTextLabel;
import com.savoycraft.tempoGui.event.TGEvent;
import com.savoycraft.theater.Theater;

/**
 * @author BJ
 * 
 */
public class TheaterGui extends TGFrame {
	private Theater theater;
	private TGButton showsButton, troupeButton;

	public TheaterGui(GuiScreen backScreen, final Theater theater) {
		super(backScreen, "FenceFoil Theatre");
		this.theater = theater;
		setFrameSize(200, 130);

		//add(new TGTextLabel(getFrameWidth() / 2, 5, "FenceFoil Theatre"));

		showsButton = new TGButton(getFrameWidth() / 2 - 75, 30, 150, 20);
		final TheaterGui thisGui = this;
		showsButton.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				Minecraft.getMinecraft().displayGuiScreen(
						new ShowsGui(thisGui, theater));
			}
		});
		add(showsButton);

		troupeButton = new TGButton(getFrameWidth() / 2 - 75, 55, 150, 20);
		add(troupeButton);

		add(new TGButton(10, 90, 70, 20, "Rename"));
		add(new TGButton(getFrameWidth() - 70 - 10, 90, 70, 20, "Orchestra"));

		// add(new TGButton(25, 5, 150, 20, "New Course")
		// .addListener(new TGListener() {
		//
		// @Override
		// public void onTGEvent(TGEvent event) {
		// mc.displayGuiScreen(new TDEditCourseGui(null));
		// }
		// }));
		// add(new TGButton(25, 30, 150, 20, "Edit Course"));
		// add(new TGButton(25, 55, 150, 20, "Settings"));
		// add(new TGButton(25, 80, 150, 20, "Update"));

		updateButtonLabels();
	}

	private void updateButtonLabels() {
		showsButton.setLabel("Shows (" + theater.getShows().size() + ")");
		troupeButton.setLabel("Troupe (" + theater.getTroupe().size() + ")");
	}
}
