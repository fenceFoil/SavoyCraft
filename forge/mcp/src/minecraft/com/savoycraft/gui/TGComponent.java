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

import java.util.HashSet;

import org.lwjgl.opengl.GL11;

import com.savoycraft.resources.TDTextureManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

/**
 * 
 *
 */
public class TGComponent extends Gui {
	/**
	 * The relative position of the component within the frame
	 */
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	/**
	 * r g b a
	 */
	protected float[] fgColor = { 0, 1, 0, 1 };
	protected float[] bgColor = { 0.2f, 0.2f, 0.2f, 1.0f };

	/**
	 * The offset required to show the component within a frame on the screen.
	 */
	private int xScreenOffset;
	private int yScreenOffset;

	private HashSet<TGListener> listeners = new HashSet<TGListener>();

	public TGComponent(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public TGComponent(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @param l
	 * @return this component, to allow for chaining
	 */
	public TGComponent addListener(TGListener l) {
		listeners.add(l);
		return this;
	}

	public void removeListener(TGListener l) {
		listeners.remove(l);
	}

	public void removeListeners() {
		listeners.clear();
	}

	void fireTGEvent(TGEvent e) {
		for (TGListener l : listeners) {
			l.onTGEvent(e);
		}
	}

	public int getScreenX() {
		return x + xScreenOffset;
	}

	public int getScreenY() {
		return y + yScreenOffset;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public int getScreenOffsetX() {
		return xScreenOffset;
	}

	public void setxScreenOffset(int xScreenOffset) {
		this.xScreenOffset = xScreenOffset;
	}

	public int getScreenOffsetY() {
		return yScreenOffset;
	}

	public void setScreenOffset(int x, int y) {
		setxScreenOffset(x);
		setyScreenOffset(y);
	}

	public void setyScreenOffset(int yScreenOffset) {
		this.yScreenOffset = yScreenOffset;
	}

	public float[] getFgColor() {
		return fgColor;
	}

	public void setFgColor(float[] fgColor) {
		this.fgColor = fgColor;
	}

	public float[] getBgColor() {
		return bgColor;
	}

	public void setBgColor(float[] bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Draws the component
	 * 
	 * @param mx
	 *            compared to 0, 0 of frame; not raw screen position. If the
	 *            frame is at 100, 100 on the screen, and mouse is at 140, 140
	 *            on the screen, pass 40, 40 to mx, my
	 * @param my
	 */
	public void draw(int mx, int my) {
		;
	}

	public void keyTyped(char keyChar, char keyCode) {

	}

	/**
	 * Called when the mouse is clicked.
	 */
	public void mouseClicked(int mx, int my, int button) {
	}

	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	public void mouseMovedOrUp(int mx, int my, int button) {
	}

	/**
	 * 
	 * @param mx
	 *            compared to 0, 0 of frame; not raw screen position. If the
	 *            frame is at 100, 100 on the screen, and mouse is at 140, 140
	 *            on the screen, pass 40, 40 to mx, my
	 * @param my
	 * @return
	 */
	public boolean isMouseInside(int mx, int my) {
		if (mx >= x && mx <= x + width) {
			if (my >= y && my <= y + height) {
				return true;
			}
		}
		return false;
	}

	protected void setGLColorFG() {
		GL11.glColor4f(fgColor[0], fgColor[1], fgColor[2], fgColor[3]);
	}

	protected void setGLColorBG() {
		GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);
	}

	/**
	 * Draws the background of the component, tiling the given texture
	 * 
	 * @param texture
	 *            texture path in jar
	 * @param tu
	 *            upper right corner of tile in texture (remembering minecraft's
	 *            convention of assuming every texture is 256x256)
	 * @param tv
	 * @param tWidth
	 *            size of tile in texture
	 * @param tHeight
	 */
	protected void drawBackground(String texture, int tu, int tv, int tWidth,
			int tHeight) {
		// Bind and set up texture
		setGLColorBG();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		// Draw complete tiles
		for (int i = 0; i < width / tWidth; i++) {
			for (int j = 0; j < height / tHeight; j++) {
				drawTexturedModalRect(xScreenOffset + x + i * tWidth,
						yScreenOffset + y + j * tHeight, tu, tv, tWidth,
						tHeight);
			}
		}

		// Draw partial tiles
		int partialRight = width % tWidth;
		int partialBottom = height % tHeight;
		// Right
		if (partialRight > 0) {
			for (int y = 0; y < height / tHeight; y++) {
				drawTexturedModalRect(xScreenOffset + x + (width / tWidth)
						* tWidth, yScreenOffset + this.y + y * tHeight, tu, tv,
						partialRight, tHeight);
			}
		}
		// Bottom
		if (partialBottom > 0) {
			for (int i = 0; i < width / tWidth; i++) {
				drawTexturedModalRect(xScreenOffset + x + tWidth * i,
						yScreenOffset + y + height - partialBottom, tu, tv,
						tWidth, partialBottom);
			}
		}
		// Bottom right corner
		if (partialBottom > 0 || partialRight > 0) {
			drawTexturedModalRect(xScreenOffset + x + width - partialRight,
					yScreenOffset + y + height - partialBottom, tu, tv,
					partialRight, partialBottom);
		}
	}

	/**
	 * 
	 * @param components
	 *            4-element float array, rgba
	 * @return
	 */
	public static int getColorAsInt(float[] components) {
		int color = (int) (components[2] * 255f);
		color += (int) (components[1] * 255f) << 8;
		color += (int) (components[0] * 255f) << 16;
		color += (int) (components[3] * 255f) << 24;
		return color;
	}

	/**
	 * Equivilant to drawBorder(0)
	 */
	protected void drawBorder() {
		drawBorder(0);
	}

	/**
	 * Draws a 1 pixel thick foreground-colored border around component
	 * 
	 * @param expand
	 *            can be negative
	 */
	protected void drawBorder(int expand) {
		// Draw sides, ignoring corners
		drawVerticalLine(x + xScreenOffset - expand,
				y + yScreenOffset - expand,
				y + yScreenOffset + height + expand, getColorAsInt(fgColor));
		drawVerticalLine(x + xScreenOffset + width + expand - 1, y
				+ yScreenOffset, y + yScreenOffset + height - 1,
				getColorAsInt(fgColor));
		// Draw top and bottom
		drawHorizontalLine(x + xScreenOffset - expand, x + xScreenOffset
				+ width + expand - 1, y + yScreenOffset - expand,
				getColorAsInt(fgColor));
		drawHorizontalLine(x + xScreenOffset - expand, x + xScreenOffset
				+ width + expand - 1, y + yScreenOffset + height + expand - 1,
				getColorAsInt(fgColor));
	}

	protected void playSelectSound() {
		Minecraft.getMinecraft().sndManager.playSoundFX("note.snare", 1.0F,
				1.0f);
	}
}
