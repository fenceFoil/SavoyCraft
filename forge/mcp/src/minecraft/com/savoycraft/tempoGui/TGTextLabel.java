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

import com.savoycraft.resources.TDTextureManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 *
 */
public class TGTextLabel extends TGComponent {

	private static final int MARGINS = 2;
	private static final int TEXT_HEIGHT = 8;
	protected String labelText;

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TGTextLabel(int centerX, int y, String text) {
		setY(y);
		setLabelText(text);
		setHeight(TEXT_HEIGHT + MARGINS);

		// X is determined in part by the size of the text
		int textWidth = Minecraft.getMinecraft().fontRenderer
				.getStringWidth(labelText);
		setWidth(textWidth + MARGINS);
		setX(centerX - getWidth() / 2);
	}

	@Override
	public void draw(int mx, int my) {
		super.draw(mx, my);

		// // Draw wool background
		// drawBackground(TDTextureManager.GUI_TEX_1, 0, 0, 16, 16, mx, my);

		// Draw text label
		int color = labelColor;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int textX = getScreenX() + MARGINS;
		int textY = getScreenY() + MARGINS;
		fr.drawString(getLabelText(), textX, textY, color);
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

}