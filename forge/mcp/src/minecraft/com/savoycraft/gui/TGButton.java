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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

import com.savoycraft.resources.TDTextureManager;

/**
 * @author BJ
 * 
 */
public class TGButton extends TGComponent {

	private boolean pressing;
	private String label;

	public TGButton(int x, int y, int width, int height, String label) {
		super(x, y, width, height);
		this.label = label;
	}

	/**
	 * Makes a new TGButton, determining the width from the label
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param label
	 */
	public TGButton(int x, int y, int height, String label) {
		super(x, y);
		setHeight(height);
		setLabel(label);

		// Calculate width
		int labelWidth = Minecraft.getMinecraft().fontRenderer
				.getStringWidth(label);
		// Width is label width + 2 pixel margins at either end for border + 1
		// pixel extra margin at either end
		setWidth(labelWidth + 6);
	}

	@Override
	public void draw(int mx, int my) {
		super.draw(mx, my);

		// Draw wool background
		drawBackground(TDTextureManager.GUI_TEX_1, 0, 0, 16, 16);

		// Draw border
		drawBorder(pressing ? -1 : 0);

		// Draw text label
		int color = getColorAsInt(fgColor);
		if (pressing) {
			color = getColorAsInt(bgColor);
		}
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int labelWidth = fr.getStringWidth(label);
		int textX = getScreenX() + (width - labelWidth) / 2;
		int textY = getScreenY() + (height / 2) - 4;
		fr.drawString(label, textX, textY, color);
	}

	@Override
	public void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);

		if (button == 0 && isMouseInside(mx, my) && !pressing) {
			// Start pressing
			pressing = true;
			playSelectSound();
		}
	}

	@Override
	public void mouseMovedOrUp(int mx, int my, int button) {
		super.mouseMovedOrUp(mx, my, button);

		if (button == 0 && pressing) {
			// Raised mouse button
			if (isMouseInside(mx, my)) {
				fireTGEvent(new TGEvent(this));
			}
			pressing = false;
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
