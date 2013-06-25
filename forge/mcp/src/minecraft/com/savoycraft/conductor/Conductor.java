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
package com.savoycraft.conductor;

import static com.esotericsoftware.minlog.Log.info;

import java.util.EnumSet;
import java.util.LinkedList;

import com.savoycraft.SavoyCraft;
import com.savoycraft.cue.Cue;
import com.savoycraft.opera.Opera;
import com.savoycraft.opera.OperaManager;
import com.savoycraft.opera.OperaScene;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

/**
 * @author BJ
 * 
 */
public class Conductor implements IScheduledTickHandler {
	private AudioPlayer audioPlayer;

	private String charHandle;
	private String sceneHandle;
	private Opera opera;
	private OperaScene scene;
	private boolean sceneStarted = false;
	
	//private static double LATENCY_CORRECTION = 1d;

	public Conductor() {
	}

	/**
	 * 
	 * @param handle
	 *            two part handle of the form OPERA:SCENE
	 */
	public void playScene(String handle) {
		String[] handles = handle.split(":");

		opera = OperaManager.getOpera(handles[0]);

		if (opera == null) {
			info("Conductor cannot play handle: " + handle);
		}

		scene = opera.getScene(handles[1]);
		sceneHandle = scene.getHandle();
		charHandle = scene.getFeaturedCharacters().getFirst();

		SavoyCraft.getBotManager().clearBots();
		SavoyCraft.getBotManager().createBot(charHandle,
				opera.getCharacter(charHandle).getName());

		sceneStarted = false;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	private double lastTickTime = 0;

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// If there is an active scene
		if (sceneHandle != null) {

			// If the scene needs to be set up and begun
			if (!sceneStarted) {
				sceneStarted = true;

				// Begin the music
				if (audioPlayer != null) {
					audioPlayer.stopPlayer();
				}

				audioPlayer = new AudioPlayer(
						opera.getMusicForScene(sceneHandle));
				audioPlayer.start();

				lastTickTime = 0;
			}

			// Forward any new cues to bots
			double currTickTime = audioPlayer.getPosition() - 0.3;

			LinkedList<Cue> cues = scene.getCues(lastTickTime, currTickTime);
			SavoyCraft.getBotManager().executeCues(cues);

			lastTickTime = currTickTime;
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "SavoyCraft Conductor";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}

}
