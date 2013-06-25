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

import com.savoycraft.opera.Opera;
import com.savoycraft.opera.OperaManager;
import com.savoycraft.opera.OperaScene;
import com.savoycraft.tempoGui.TGButton;
import com.savoycraft.tempoGui.TGFrame;
import com.savoycraft.tempoGui.TGList;
import com.savoycraft.tempoGui.TGListener;
import com.savoycraft.tempoGui.TGPanel;
import com.savoycraft.tempoGui.TGTextLabel;
import com.savoycraft.tempoGui.event.TGEvent;
import com.savoycraft.tempoGui.event.TGListEvent;
import com.savoycraft.theater.Show;
import com.savoycraft.theater.ShowScene;
import com.savoycraft.theater.Theater;

/**
 * @author BJ
 * 
 */
public class NewShowGui extends TGFrame {
	private LinkedList<Opera> operas;
	private TGList showList;
	private TGList operaList;
	private TGPanel scenePanel;
	private Theater theater;
	private Show show;
	private static final int listY = 25;
	private static final int bottomMargin = 25;

	private Show newShow = new Show();
	private TGPanel operaListPanel;

	public NewShowGui(GuiScreen backScreen, Theater theater, Show show) {
		super(backScreen, "New Show");
		setFrameSize(300, 150);
		setTheater(theater);
		setShow(show);
	}

	protected void setupCompoenents() {

		final GuiScreen thisGui = this;

		removeAll();
		add(new TGTextLabel(getFrameWidth() / 2, 5, "Choose Songs:"));

		showList = new TGList(0, listY, getFrameWidth() / 2, getFrameHeight()
				- bottomMargin * 2, true, bottomMargin,
				new LinkedList<String>());
		showList.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				if (event instanceof TGListEvent) {
					TGListEvent e = (TGListEvent) event;
					newShow.getScenes().remove(e.getIndex());
					updateShowList();
				}
			}
		});
		add(showList);

		// Add okay and cancel buttons
		final GuiScreen backscreenfinal = backScreen;
		add(new TGButton(2, getFrameHeight() - 22, getFrameWidth() / 4 - 4, 20,
				"OK").addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				Minecraft.getMinecraft().displayGuiScreen(
						new NewShowNameGui(thisGui, theater, show));
			}
		}));
		add(new TGButton(getFrameWidth() / 4 + 2, getFrameHeight() - 22,
				getFrameWidth() / 4 - 4, 20, "Cancel")
				.addListener(new TGListener() {

					@Override
					public void onTGEvent(TGEvent event) {
						Minecraft.getMinecraft().displayGuiScreen(
								backscreenfinal);
					}
				}));

		operaListPanel = new TGPanel(getFrameWidth() / 2, 0,
				getFrameWidth() / 2, getFrameHeight(), false);
		operaListPanel.setZLevel(0.5);

		// List of operas
		operas = OperaManager.getOperaList();
		// Get list of opera names from ordered list just obtained
		LinkedList<String> operaTitles = new LinkedList<String>();
		for (Opera o : operas) {
			operaTitles.add(o.getName());
		}

		// Create list component
		operaList = new TGList(0, listY, getFrameWidth() / 2, getFrameHeight()
				- bottomMargin * 2, false, bottomMargin, operaTitles);
		operaList.setButtonHeight(20);
		operaList.updateItems();
		operaListPanel.add(operaList);

		// When a entry in the opera list is clicked, hide the opera list panel
		// and show a new panel containing a list of the selected opera's scenes
		// and various controls
		final NewShowGui thisFrame = this;
		operaList.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				if (event instanceof TGListEvent) {
					TGListEvent e = (TGListEvent) event;
					Opera selectedOpera = operas.get(e.getIndex());

					// Hide list of operas
					thisFrame.remove(operaListPanel);

					// Show new panel
					showScenePanel(selectedOpera);
				}
			}
		});

		// Add opera list panel to frame
		add(operaListPanel);
	}

	protected void showScenePanel(Opera selectedOpera) {

		remove(scenePanel);
		scenePanel = new TGPanel(getFrameWidth() / 2, 0, getFrameWidth() / 2,
				getFrameHeight(), false);
		scenePanel.setZLevel(0.5);
		scenePanel.removeAll();

		final LinkedList<OperaScene> scenes = (LinkedList<OperaScene>) selectedOpera
				.getScenes().clone();
		LinkedList<String> sceneTitles = new LinkedList<String>();
		for (OperaScene s : scenes) {
			sceneTitles.add(s.getName());
		}

		TGList sceneList = new TGList(0, listY, scenePanel.getWidth(),
				getFrameHeight() - bottomMargin * 2, false, bottomMargin,
				sceneTitles);
		sceneList.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				if (event instanceof TGListEvent) {
					TGListEvent e = (TGListEvent) event;
					addSceneToShow(scenes.get(e.getIndex()));
				}
			}
		});

		scenePanel.add(sceneList);

		TGButton addAllButton = new TGButton(scenePanel.getWidth() / 2 + 2,
				getFrameHeight() - 22, scenePanel.getWidth() / 2 - 4, 20,
				"Add All");
		addAllButton.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				for (OperaScene os : scenes) {
					addSceneToShow(os);

					// Push back button
					remove(scenePanel);
					add(operaListPanel);
				}
			}
		});
		scenePanel.add(addAllButton);

		TGButton backButton = new TGButton(2, getFrameHeight() - 22,
				scenePanel.getWidth() / 2 - 4, 20, "Back");
		backButton.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				remove(scenePanel);
				add(operaListPanel);
			}
		});
		scenePanel.add(backButton);

		add(scenePanel);
	}

	protected void addSceneToShow(OperaScene scene) {
		newShow.getScenes().add(new ShowScene(scene));
		updateShowList();
	}

	private void updateShowList() {
		LinkedList<String> showSceneNames = new LinkedList<String>();
		for (ShowScene s : newShow.getScenes()) {
			showSceneNames.add(OperaManager.getScene(s.getSceneID()).getName());
			System.out.println(OperaManager.getScene(s.getSceneID()).getName());
		}
		showList.setItems(showSceneNames);
		showList.updateItems();
	}

	@Override
	public void initGui() {
		super.initGui();

		setFrameSize(width, height);

		setupCompoenents();
	}

	public Theater getTheater() {
		return theater;
	}

	public void setTheater(Theater theater) {
		this.theater = theater;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}
}
