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
package com.savoycraft.opera;

import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.savoycraft.cue.Cue;
import com.savoycraft.cue.NoteCue;
import com.savoycraft.util.DOMUtil;

/**
 *
 */
public class Scene {

	private int pos = 0;
	private String handle = "";

	/**
	 * Stores one beat track per character in the scene
	 */
	private HashMap<String, BeatTrack> characterBeats = new HashMap<String, BeatTrack>();

	private LinkedList<String> featuredCharacters = new LinkedList<String>();

	public static Scene load(Element sceneElement) {
		NodeList sceneNodes = sceneElement.getChildNodes();

		Scene scene = new Scene();
		scene.setHandle(Opera.getTextElement("handle", sceneNodes));

		// Load characters
		LinkedList<Element> characterElements = DOMUtil.findElements(
				"character", sceneNodes);
		for (Element characterElement : characterElements) {
			String character = DOMUtil.getAttributeValue(characterElement,
					"name");

			scene.featuredCharacters.add(character);

			Element beatElement = DOMUtil.findElement("notebeats",
					characterElement.getChildNodes());
			if (beatElement != null) {
				BeatTrack t = BeatTrack.load(beatElement);
				if (t != null) {
					scene.characterBeats.put(character, t);
				}
			}
		}

		return scene;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * A list of character handles
	 * 
	 * @return shallow copy of the list
	 */
	public LinkedList<String> getFeaturedCharacters() {
		return (LinkedList<String>) featuredCharacters.clone();
	}

	/**
	 * Fetches the cues that are to execute during the given interval
	 * 
	 * @param startTime
	 *            inclusive (seconds)
	 * @param endTime
	 *            exclusive (seconds)
	 * @return the cues; never null
	 */
	public LinkedList<Cue> getCues(double startTime, double endTime) {
		LinkedList<Cue> found = new LinkedList<Cue>();

		// Check for character note beats
		for (String charHandle : characterBeats.keySet()) {
			BeatTrack beats = characterBeats.get(charHandle);
			if (beats != null) {
				LinkedList<Double> relevantBeats = beats.getTimes(startTime,
						endTime);
				for (Double d : relevantBeats) {
					found.add(new NoteCue(charHandle, d));
				}
			}
		}

		return found;
	}

}
