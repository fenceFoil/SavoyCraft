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
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quint;
import aurelienribon.tweenengine.equations.Sine;

import com.savoycraft.TDConfig;
import com.savoycraft.resources.TDTextureManager;
import com.savoycraft.resources.UpdateResourcesThread;

/**
 * Handles basic tasks for a SavoyCraft gui, and displays a decorative
 * background in the center of the screen among other novelties. Creates a
 * generic framework for arranging and displaying components in a grid within
 * the decorative background.
 */
public class TGFrame extends GuiScreen {

	public static Random rand = new Random();

	static {
		Tween.registerAccessor(EQBar.class, new EQBarTweenAccessor());
	}

	private static final int FADE_OUT_WIDTH = 7;
	private static final int ROPE_HEIGHT = 5;
	private String typedKeys = "";
	private GuiScreen backScreen;

	/**
	 * Pixel dimensions of centered "frame:" decorative background which
	 * components are displayed in.
	 */
	private int frameWidth = 300, frameHeight = 200;

	private int frameColor = 0xddeeeeaa;

	private String frameTitle = "SavoyCraft";

	private LinkedList<EQBar> leftEQBars = new LinkedList<EQBar>();

	private LinkedList<EQBar> rightEQBars = new LinkedList<EQBar>();

	private LinkedList<EQBar> leftCurtain = new LinkedList<EQBar>();
	private LinkedList<EQBar> rightCurtain = new LinkedList<EQBar>();

	private HashSet<TGComponent> components = new HashSet<TGComponent>();

	public TGFrame(GuiScreen backScreen) {
		this.backScreen = backScreen;
	}

	@Deprecated
	public TGFrame(GuiScreen backScreen, int width, int height) {
		this.backScreen = backScreen;
		setFrameSize(width, height);
	}

	public void setUpEQBars() {
		// Set up eq bars
		int numBars = frameHeight / 16;

		leftEQBars.clear();
		rightEQBars.clear();
		for (int i = 0; i < numBars; i++) {
			EQBar eqbar = new EQBar(0, 0, true);
			leftEQBars.add(eqbar);

			EQBar reqbar = new EQBar(0, 0, false);
			rightEQBars.add(reqbar);
		}
	}

	private void updateEQBarPositions() {
		int numBars = Math.min(leftEQBars.size(), rightEQBars.size());
		int barOffset = (frameHeight - numBars * 16) / 2;

		int barLeftX = getFrameX();
		int barRightX = getFrameX() + getFrameWidth();
		for (int i = 0; i < numBars; i++) {
			int barY = getFrameY() + barOffset + i * 16;

			leftEQBars.get(i).setX(barLeftX);
			leftEQBars.get(i).setY(barY);
			rightEQBars.get(i).setX(barRightX);
			rightEQBars.get(i).setY(barY);
		}

		// Set up curtain bars
		if (leftCurtain.size() == 0 && height != 0) {
			leftCurtain.clear();
			rightCurtain.clear();

			int curtainBars = (height / 16) + 1;
			for (int i = 0; i < curtainBars; i++) {
				EQBar eqbar = new EQBar(0, 0, false);
				if (i != ROPE_HEIGHT) {
					eqbar.setRGBA(1.0f, 0, 0, 0.7f);
				} else {
					eqbar.setRGBA(1.0f, 1.0f, 0, 0.7f);
				}
				// eqbar.setProtrude(4);
				leftCurtain.add(eqbar);

				EQBar reqbar = new EQBar(0, 0, true);
				if (i != ROPE_HEIGHT) {
					reqbar.setRGBA(1.0f, 0, 0, 0.7f);
				} else {
					reqbar.setRGBA(1.0f, 1.0f, 0, 0.7f);
				}
				// reqbar.setProtrude(4);
				rightCurtain.add(reqbar);
			}
		}

		// Curtain positions
		numBars = Math.min(leftCurtain.size(), rightCurtain.size());
		barOffset = 0;
		barLeftX = 0;
		barRightX = width;
		for (int i = 0; i < numBars; i++) {
			int barY = i * 16;

			leftCurtain.get(i).setX(barLeftX);
			leftCurtain.get(i).setY(barY);
			rightCurtain.get(i).setX(barRightX);
			rightCurtain.get(i).setY(barY);
		}
	}

	@Override
	public void drawScreen(int mx, int my, float par3) {
		updateTweens();

		// draw background
		// drawMinetunesBackground(width, height);
		drawEQBars();
		drawFrameBackground(frameWidth, frameHeight);

		drawTitle();

		// Draw buttons
		super.drawScreen(mx, my, par3);

		for (TGComponent c : components) {
			c.draw(mx - getFrameX(), my - getFrameY());
		}
	}

	private void drawTitle() {
		if (frameTitle != null) {
			// Draw box around title string, with a 2 pixel margin
			drawRectSideFade(
					getFrameX()
							+ (getFrameWidth() - fontRenderer.getStringWidth(frameTitle))
							/ 2 - 2, getFrameY() - 10 - 2,
					fontRenderer.getStringWidth(frameTitle) + 4, 10 + 2,
					FADE_OUT_WIDTH, frameColor);
			// drawString(
			// fontRenderer,
			// frameTitle,
			// getFrameX()
			// + (getFrameWidth() - fontRenderer
			// .getStringWidth(frameTitle)) / 2,
			// getFrameY() - 10, 0xffaaaaff);
			fontRenderer.drawString(
					frameTitle,
					getFrameX()
							+ (getFrameWidth() - fontRenderer
									.getStringWidth(frameTitle)) / 2,
					getFrameY() - 10, 0xff0000ff, false);
		}
	}

	public String getFrameTitle() {
		return frameTitle;
	}

	public void setFrameTitle(String frameTitle) {
		this.frameTitle = frameTitle;
	}

	private void drawRectSideFade(int x, int y, int width, int height,
			int fadePixels, int mainColor) {
		drawRect(x, y, x + width, y + height, mainColor);
		drawGradientRectH(x - fadePixels, y, x, y + height, 0x00000000,
				mainColor);
		drawGradientRectH(x + width, y, x + width + FADE_OUT_WIDTH, y + height,
				mainColor, 0x00000000);
	}

	private void drawEQBars() {
		for (EQBar b : leftCurtain) {
			b.draw();
		}
		for (EQBar b : rightCurtain) {
			b.draw();
		}
		for (EQBar b : leftEQBars) {
			b.draw();
		}
		for (EQBar b : rightEQBars) {
			b.draw();
		}

	}

	private void drawFrameBackground(int width, int height) {
		drawRectSideFade(getFrameX(), getFrameY(), getFrameWidth(),
				getFrameHeight(), FADE_OUT_WIDTH, frameColor);
	}

	/**
	 * Draws a rectangle with a HORIZONTAL gradient between the specified
	 * colors.
	 * 
	 * From MOJANG code
	 */
	protected void drawGradientRectH(int par1, int par2, int par3, int par4,
			int par5, int par6) {
		float f = (float) (par5 >> 24 & 255) / 255.0F;
		float f1 = (float) (par5 >> 16 & 255) / 255.0F;
		float f2 = (float) (par5 >> 8 & 255) / 255.0F;
		float f3 = (float) (par5 & 255) / 255.0F;
		float f4 = (float) (par6 >> 24 & 255) / 255.0F;
		float f5 = (float) (par6 >> 16 & 255) / 255.0F;
		float f6 = (float) (par6 >> 8 & 255) / 255.0F;
		float f7 = (float) (par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double) par3, (double) par2,
				(double) this.zLevel);
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex((double) par1, (double) par2,
				(double) this.zLevel);
		tessellator.addVertex((double) par1, (double) par4,
				(double) this.zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double) par3, (double) par4,
				(double) this.zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public int getFrameX() {
		return (width - frameWidth) / 2;
	}

	public int getFrameY() {
		return (height - frameHeight) / 8 * 5;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
		updateEQBarPositions();
		updateComponentOffsets();
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
		setUpEQBars();
		updateEQBarPositions();
		updateComponentOffsets();
	}

	public void updateComponentOffsets() {
		for (TGComponent c : components) {
			c.setxScreenOffset(getFrameX());
			c.setyScreenOffset(getFrameY());
		}
	}

	@Override
	protected void keyTyped(char charIn, int codeIn) {
		// Handle escape
		if (codeIn == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(backScreen);
		}

		// Check for combos
		// updateCombos(charIn);
	}

	private void updateCombos(char par1) {
		typedKeys += par1;
		if (typedKeys.toLowerCase().endsWith("resources")) {
			// Re-download resources
			Minecraft.getMinecraft().sndManager.playSoundFX("note.snare", 1.0F,
					1.0f);
			TDConfig.setInt("resources.lastZipDownloaded", -1);
			UpdateResourcesThread t = new UpdateResourcesThread();
			t.start();
			// } else if (typedKeys.toLowerCase().endsWith("clearcache")) {
			// // Clear cache for updating things
			// Minecraft.getMinecraft().sndManager.playSoundFX("note.snare",
			// 1.0F,
			// 1.0f);
			// FileUpdater.clearStaticCache();
			// Minetunes.autoUpdater.clearCache();
			// Minetunes.tutorialUpdater.clearCache();
			// } else if (typedKeys.toLowerCase().endsWith("getdev")) {
			// // Try to look for dev versions of minetunes on auto-update
			// Minecraft.getMinecraft().sndManager.playSoundFX("note.snare",
			// 1.0F,
			// 1.0f);
			// Minetunes.autoUpdater.setSpecialMode("dev");
			// checkForUpdates();
			// } else if (typedKeys.toLowerCase().endsWith("gettest")) {
			// // Try to look for test versions of minetunes on auto-update
			// Minecraft.getMinecraft().sndManager.playSoundFX("note.snare",
			// 1.0F,
			// 1.0f);
			// Minetunes.autoUpdater.setSpecialMode("test");
			// checkForUpdates();
			// } else if
			// (typedKeys.toLowerCase().endsWith("shutupandautoupdate")) {
			// Minecraft.getMinecraft().sndManager.playSoundFX("note.snare",
			// 1.0F,
			// 1.0f);
			// Minetunes.autoUpdater.clearAlreadyTriedFlag();
			// } else if (typedKeys.toLowerCase().endsWith("cga")) {
			// mc.fontRenderer = new FontRenderer(mc.gameSettings,
			// "/com/minetunes/resources/textures/CGAFont.png",
			// mc.renderEngine, false);
			// FontRendererUtils.changeCharWidth(8, mc.fontRenderer);
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);

		for (TGComponent c : components) {
			c.mouseClicked(x - getFrameX(), y - getFrameY(), button);
		}

	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);

		for (TGComponent c : components) {
			c.mouseMovedOrUp(par1 - getFrameX(), par2 - getFrameY(), par3);
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {

	}

	/**
	 * Test: Should this be called, more correctly, "resize gui?"
	 */
	@Override
	public void initGui() {
		// Fix EQ bar positions
		if (leftEQBars.size() <= 0) {
			setUpEQBars();
		}

		updateEQBarPositions();

		// Update components
		for (TGComponent c : components) {
			c.setScreenOffset(getFrameX(), getFrameY());
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	public TweenManager tweenManager = new TweenManager();
	private long lastTweenUpdate = System.currentTimeMillis();

	private void updateTweens() {
		tweenManager.update(System.currentTimeMillis() - lastTweenUpdate);
		lastTweenUpdate = System.currentTimeMillis();
	}

	private boolean pulsedEQ = false;

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (!pulsedEQ) {
			// pulseEQ(1, false);
			animateCurtains(true);
			pulsedEQ = true;
		}
	}

	public void setFrameSize(int width, int height) {
		setFrameWidth(width);
		setFrameHeight(height);

		// Update component positions
		for (TGComponent c : components) {
			c.setScreenOffset(getFrameX(), getFrameY());
		}
	}

	public void add(TGComponent component) {

		components.add(component);
		component.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				pulseEQ(1, false);
			}
		});
	}

	protected void animateCurtains(boolean opening) {
		int numBars = leftCurtain.size();

		float maxProtrude = (float) width / 16f / 2f;
		float ropeProtrude = 1.5f;
		float bottomProtrude = 2.5f;

		int duration = 3000;
		TweenEquation equation = Sine.OUT;
		int delay = 0;

		// XXX: WARNING: CRASH ISSUES IF FEWER BARS THAN ROPE HEIGHT
		// Above rope
		for (int i = 0; i < ROPE_HEIGHT; i++) {
			float protrude = maxProtrude
					- ((maxProtrude - ropeProtrude) * ((float) i / (float) ROPE_HEIGHT));
			leftCurtain.get(i).setProtrude(maxProtrude);
			Tween.to(leftCurtain.get(i),
					EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE, duration)
					.target(protrude).delay(delay).ease(equation)
					.start(tweenManager);

			rightCurtain.get(i).setProtrude(maxProtrude);
			Tween.to(rightCurtain.get(i),
					EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE, duration)
					.target(protrude).delay(delay).ease(equation)
					.start(tweenManager);

		}
		// Rope
		leftCurtain.get(ROPE_HEIGHT).setProtrude(maxProtrude);
		rightCurtain.get(ROPE_HEIGHT).setProtrude(maxProtrude);
		Tween.to(leftCurtain.get(ROPE_HEIGHT),
				EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE, duration)
				.target(ropeProtrude).delay(delay).ease(equation)
				.start(tweenManager);
		Tween.to(rightCurtain.get(ROPE_HEIGHT),
				EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE, duration)
				.target(ropeProtrude).delay(delay).ease(equation)
				.start(tweenManager);
		// Below rope
		for (int i = ROPE_HEIGHT + 1; i < numBars; i++) {
			float protrude = bottomProtrude
					- ((bottomProtrude - ropeProtrude) * (1f - ((float) (i - ROPE_HEIGHT) / (float) (numBars - ROPE_HEIGHT))));
			leftCurtain.get(i).setProtrude(maxProtrude);
			Tween.to(leftCurtain.get(i),
					EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE, duration)
					.target(protrude).delay(delay).ease(equation)
					.start(tweenManager);
			rightCurtain.get(i).setProtrude(maxProtrude);
			Tween.to(rightCurtain.get(i),
					EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE, duration)
					.target(protrude).delay(delay).ease(equation)
					.start(tweenManager);

		}
	}

	protected void pulseEQ(int count, final boolean sound) {
		TweenEquation[] equations = new TweenEquation[] { Sine.INOUT,
				Quint.INOUT, Quad.INOUT };

		for (int i = 0; i < Math.min(leftEQBars.size(), rightEQBars.size()); i++) {
			EQBar eqbar = leftEQBars.get(i);
			EQBar reqbar = rightEQBars.get(i);

			float startP = 0.5f;
			TweenEquation equation = equations[rand.nextInt(equations.length)];
			float target = rand.nextFloat() * 1.0f + startP + 0.2f;
			for (EQBar b : new EQBar[] { eqbar, reqbar }) {
				b.setProtrude(startP);

				Timeline.createSequence()
						.push(Tween.call(new TweenCallback() {

							@Override
							public void onEvent(int arg0, BaseTween<?> arg1) {
								if (sound) {
									Minecraft.getMinecraft().sndManager
											.playSoundFX("note.bass", 1.0F,
													0.5f);
								}
							}
						}))
						.push(Tween
								.to(b, EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE,
										200).target(target).ease(equation))
						.push(Tween
								.to(b, EQBarTweenAccessor.TWEEN_TYPE_PROTRUDE,
										500).target(startP).ease(Linear.INOUT))
						.repeat(count - 1, 0).start(tweenManager);
			}
		}
	}

	private class EQBar {

		private double protrude = 0;
		private int x = 0;
		private int y = 0;
		private float r = 0;
		private float g = 0;
		private float b = 1.0f;
		private float a = 0.75f;
		private boolean left = true;

		public EQBar(int x, int y, boolean left) {
			setX(x);
			setY(y);
			setLeft(left);
		}

		public void draw() {
			if (protrude <= 0) {
				return;
			}

			// Bind and set up texture
			GL11.glColor4f(r, g, b, a);
			Minecraft.getMinecraft().renderEngine
					.bindTexture(TDTextureManager.GUI_TEX_1);

			// Draw texture as needed to protrude
			int startX = x;
			int protrudeRemain = (int) (protrude * 16d);
			if (left) {
				startX -= 16;
			}

			while (protrudeRemain > 0) {
				int textureWidth = 16;
				int textureU = 0;
				if (protrudeRemain < textureWidth) {
					if (left) {
						startX += textureWidth - protrudeRemain;
						textureU = textureWidth - protrudeRemain;
						textureWidth = protrudeRemain;
					} else {
						textureWidth = protrudeRemain;
					}
				}

				drawTexturedModalRect(startX, y, textureU, 0, textureWidth, 16);

				protrudeRemain -= 16;

				if (left) {
					startX -= 16;
				} else {
					startX += 16;
				}
			}

		}

		public double getProtrude() {
			return protrude;
		}

		public void setProtrude(double protrude) {
			this.protrude = protrude;
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

		public float getR() {
			return r;
		}

		public void setR(float r) {
			this.r = r;
		}

		public float getG() {
			return g;
		}

		public void setG(float g) {
			this.g = g;
		}

		public float getB() {
			return b;
		}

		public void setB(float b) {
			this.b = b;
		}

		public float getA() {
			return a;
		}

		public void setA(float a) {
			this.a = a;
		}

		public void setRGBA(float r, float g, float b, float a) {
			setR(r);
			setG(g);
			setB(b);
			setA(a);
		}

		public boolean isLeft() {
			return left;
		}

		public void setLeft(boolean left) {
			this.left = left;
		}
	}

	public static class EQBarTweenAccessor implements TweenAccessor<EQBar> {

		public static final int TWEEN_TYPE_PROTRUDE = 1;

		@Override
		public int getValues(EQBar target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case 1:
				returnValues[0] = (float) target.getProtrude();
				return 1;
			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues(EQBar target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case 1:
				target.setProtrude(newValues[0]);
				break;
			default:
				break;
			}
		}

	}

}
