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
package com.savoycraft.bot;

import java.util.HashMap;
import java.util.LinkedList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;

import com.savoycraft.cue.Cue;
import com.savoycraft.cue.NoteCue;

/**
 * Acts as a puppeteer, manipulating entities to act as characters and things in
 * a show.
 * 
 */
public class BotManager {
	private TweenManager tweenManager = new TweenManager();
	private long lastTweenUpdate = System.currentTimeMillis();

	static {
		Tween.registerAccessor(Bot.class, new BotAccessor());
	}

	private HashMap<String, Bot> bots = new HashMap<String, Bot>();

	/**
	 * 
	 * @param handle
	 *            a character handle
	 */
	public void createBot(String handle, String name) {
		PlayerBot b = new PlayerBot(name);
		bots.put(handle, b);
	}

	/**
	 * Call every tick.
	 */
	public void update() {
		// Update tweens
		long currTime = System.currentTimeMillis();
		tweenManager.update(currTime - lastTweenUpdate);

		// Update bots
		for (Bot b : bots.values()) {
			b.update();
		}

		lastTweenUpdate = currTime;
	}

	public void clearBots() {
		for (Bot b : bots.values()) {
			b.kill();
		}
		bots.clear();
	}

	public void executeCues(LinkedList<Cue> cues) {
		for (Cue cue : cues) {
			Bot targetBot = bots.get(cue.getCharHandle());
			if (targetBot != null) {
				// Try to execute the cue here
				botManagerExecuteCue(cue, targetBot);
				// Have the bot execute the cue as well
				targetBot.executeCue(cue);
			}
		}
	}

	private void botManagerExecuteCue(Cue cue, Bot targetBot) {
		if (cue instanceof NoteCue) {
			Tween.to(targetBot, BotAccessor.TWEEN_TYPE_POSY, 0.3f)
					.target((float) (targetBot.getPosY() + 0.1))
					.ease(Sine.OUT).start(tweenManager);
		}
	}
}
