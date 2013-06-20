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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.minecraft.client.gui.GuiScreen;

/**
 * @author BJ
 * 
 */
public class TDEditCourseGui extends TGFrame {

	private TGTabbedPanel tabs;

	/**
	 * @param backScreen
	 */
	public TDEditCourseGui(GuiScreen backScreen) {
		super(backScreen);

		//setFrameSize(400, 200);
		setFrameTitle("Editing Course");

		tabs = new TGTabbedPanel(0, 0, 400, 200, false);
		add(tabs);
		
		tabs.addTabs(new String[] {"Song", "Challenges", "Analysis"});
		tabs.addTo("Challenges", new TGButton(15, 35, 80, 20, "Button").addListener(new TGListener() {
			
			@Override
			public void onTGEvent(TGEvent event) {
				try {
					Desktop.getDesktop().browse(
							new URI("spotify:track:03V1Ak1eDzhC4DkrxorE8Q"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));
	}

	@Override
	public void initGui() {
		super.initGui();
		setFrameSize(width - 50, height - 50);
		tabs.setSize(width - 50, height - 50);
	}

}
