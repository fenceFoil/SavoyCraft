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

import com.savoycraft.tempoGui.TGFrame;
import com.savoycraft.theater.Show;
import com.savoycraft.theater.Theater;

import net.minecraft.client.gui.GuiScreen;

/**
 * @author BJ
 *
 */
public class ShowGui extends TGFrame {

	public ShowGui(GuiScreen thisGui, Theater theater, Show show) {
		super(thisGui);
	}

}
