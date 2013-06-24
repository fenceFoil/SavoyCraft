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

import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.savoycraft.tempoGui.TGButton;
import com.savoycraft.tempoGui.TGFrame;
import com.savoycraft.tempoGui.TGList;
import com.savoycraft.tempoGui.TGListener;
import com.savoycraft.tempoGui.event.TGAddButtonEvent;
import com.savoycraft.tempoGui.event.TGEvent;
import com.savoycraft.tempoGui.event.TGListEvent;
import com.savoycraft.theater.Show;
import com.savoycraft.theater.Theater;

/**
 * @author BJ
 * 
 */
public class ShowsGui extends TGFrame {
	private Theater theater;
	private TGButton showsButton, troupeButton;

	public ShowsGui(GuiScreen backScreen, final Theater theater) {
		super(backScreen, "Shows");
		this.theater = theater;

		final GuiScreen thisGui = this;

		// setFrameSize(200, 130);
		setFrameSize(200, 180);

		// Add list of shows
		final LinkedList<String> shows = new LinkedList<String>();
		for (Show s : theater.getShows()) {
			shows.add(s.getName());
		}

		TGList list = new TGList(0, 20, getFrameWidth(), getFrameHeight() - 40,
				false, 20, shows);
		list.setAddButtonEnabled(true);
		list.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				if (event instanceof TGAddButtonEvent) {
					Show show = new Show();
					Minecraft.getMinecraft().displayGuiScreen(
							new NewShowGui(thisGui, theater, show));
				} else if (event instanceof TGListEvent) {
					Minecraft.getMinecraft().displayGuiScreen(
							new ShowGui(thisGui, theater, theater.getShows()
									.get(((TGListEvent) event).getIndex())));
				}
			}
		});
		add(list);

		// Add back button
		final GuiScreen backScreenFinal = backScreen;
		add(new TGButton(0, getFrameHeight() - 18, getFrameWidth(), 16, "Back")
				.addListener(new TGListener() {

					@Override
					public void onTGEvent(TGEvent event) {
						Minecraft.getMinecraft().displayGuiScreen(
								backScreenFinal);
					}
				}));
	}
}
