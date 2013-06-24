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

import net.minecraft.client.gui.GuiScreen;

import com.savoycraft.tempoGui.TGButton;
import com.savoycraft.tempoGui.TGFrame;
import com.savoycraft.tempoGui.TGList;
import com.savoycraft.theater.Theater;

/**
 * @author BJ
 * 
 */
public class ShowsGui extends TGFrame {
	private Theater theater;
	private TGButton showsButton, troupeButton;

	public ShowsGui(GuiScreen backScreen, final Theater theater) {
		super(backScreen, "SavoyCraft");
		this.theater = theater;
		// setFrameSize(200, 130);
		setFrameSize(200, 180);

		LinkedList<String> shows = new LinkedList<String>();
		shows.add("The Creeperer");
		shows.add("Trial by TNT");
		shows.add("HMS Notchenjeb");
		shows.add("Pirates of Penzance");
		shows.add("Impatience");
		shows.add("Creepolanthe");
		shows.add("Princess Idle");
		shows.add("The Minekado");

		TGList list = new TGList(0, 20, getFrameWidth(), getFrameHeight() - 40,
				false, 20, shows);
		list.setAddButtonEnabled(true);
		add(list);
	}
	//
	// private void updateButtonLabels() {
	// showsButton.setLabel("Shows (" + theater.getShows().size() + ")");
	// troupeButton.setLabel("Troupe (" + theater.getTroupe().size() + ")");
	// }
}
