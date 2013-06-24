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
package com.savoycraft.tempoGui;

import java.util.LinkedList;

import com.savoycraft.tempoGui.event.TGEvent;
import com.savoycraft.tempoGui.event.TGListEvent;

/**
 * @author BJ
 * 
 */
public class TGList extends TGScrollPanel {

	private LinkedList<String> items;
	private boolean addButtonEnabled;
	private TGButton addButton;

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param showBorder
	 * @param letterboxHeight
	 */
	public TGList(int x, int y, int width, int height, boolean showBorder,
			int letterboxHeight, LinkedList<String> listItems) {
		super(x, y, width, height, showBorder, letterboxHeight);
		setItems((LinkedList<String>) listItems.clone());
		updateItems();
	}

	public void updateItems() {
		components.clear();

		int buttonHeight = 14;
		int buttonSpacing = 15;
		for (int i = 0; i < items.size(); i++) {
			final int iFinal = i;
			final TGList thisList = this;
			TGButton newButton = new TGButton(scrollbar.getWidth() + 3,
					buttonSpacing * i, getWidth() - scrollbar.getWidth() * 2
							- 3 * 2, buttonHeight, items.get(i));
			newButton.addListener(new TGListener() {

				@Override
				public void onTGEvent(TGEvent event) {
					fireTGEvent(new TGListEvent(thisList, iFinal));
				}
			});
			components.add(newButton);
		}

		if (addButtonEnabled) {
			int addButtonWidth = (int) ((float) buttonHeight * 1.6f);
			int buttonMaxWidth = getWidth() - scrollbar.getWidth() * 2 - 3 * 2;
			addButton = new TGButton(buttonMaxWidth / 2, buttonSpacing
					* items.size(), addButtonWidth, buttonHeight, "+");
			addButton.setBgColor(0xff44ff22);
			components.add(addButton);
		}
	}

	public LinkedList<String> getItems() {
		return items;
	}

	public void setItems(LinkedList<String> items) {
		this.items = items;
	}

	public boolean isAddButtonEnabled() {
		return addButtonEnabled;
	}

	public void setAddButtonEnabled(boolean addButtonEnabled) {
		this.addButtonEnabled = addButtonEnabled;

		updateItems();
	}

}
