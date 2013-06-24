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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.savoycraft.util.DOMUtil;

/**
 * Instantiate with the static load() method. Contains all of the static parts
 * of an opera: the music, the characters, the note timings, etc, which are not
 * created by the player.
 * 
 */
public class Opera {
	private String name = "";
	private String handle = "";

	private File sourceFile;

	private LinkedList<Scene> scenes = new LinkedList<Scene>();

	private LinkedList<Character> characters = new LinkedList<Character>();

	private static void createIDNum() {

	}

	/**
	 * 
	 * @param f
	 *            a directory
	 * @return either a loaded opera, or null if loading failed
	 */
	public static Opera load(File f) {
		if (!f.isDirectory()) {
			return null;
		}

		// First, find and load the main info file of the opera
		// Element infoRoot =
		// DOMUtil.getRootElement(DOMUtil.loadDocument(ZipUtil
		// .extract(f.getPath(), "info.xml")));
		Element infoRoot = null;
		try {
			infoRoot = DOMUtil.getRootElement(DOMUtil.loadDocument(new File(f,
					"info.xml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (infoRoot == null) {
			return null;
		}

		// Load properties of opera
		Opera opera = new Opera();

		opera.sourceFile = f;

		NodeList operaNodes = infoRoot.getChildNodes();

		opera.setHandle(getTextElement("handle", operaNodes));
		opera.setName(getTextElement("name", operaNodes));

		// Load characters
		LinkedList<Element> characterElements = DOMUtil.findElements(
				"character", operaNodes);
		for (Element characterElement : characterElements) {
			Character character = Character.load(characterElement);
			if (character != null) {
				opera.characters.add(character);
			}
		}

		// Load scenes
		LinkedList<Element> sceneElements = DOMUtil.findElements("scene",
				operaNodes);
		for (Element sceneElement : sceneElements) {
			Scene scene = Scene.load(sceneElement);
			if (scene != null) {
				opera.scenes.add(scene);
			}
		}
		return opera;
	}

	/**
	 * Return the text contents of the given element in the node list.
	 * 
	 * @param elementName
	 * @param nodes
	 * @return null if text inside is null or the element is not found
	 */
	public static String getTextElement(String elementName, NodeList nodes) {
		Element e = DOMUtil.findElement(elementName, nodes);
		if (e == null) {
			return null;
		} else {
			return e.getTextContent();
		}
	}

	/**
	 * Returns a shallow copy of this opera's scene list
	 * 
	 * @return
	 */
	public LinkedList<Scene> getScenes() {
		return (LinkedList<Scene>) scenes.clone();
	}

	/**
	 * Returns either the found scene, or null
	 * 
	 * @param handle
	 * @return
	 */
	public Scene getScene(String handle) {
		for (Scene s : scenes) {
			if (s.getHandle().equals(handle)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Returns a shallow copy of this opera's character list
	 * 
	 * @return
	 */
	public LinkedList<Character> getCharacters() {
		return (LinkedList<Character>) characters.clone();
	}

	/**
	 * Returns either the found character, or null
	 * 
	 * @param handle
	 * @return
	 */
	public Character getCharacter(String handle) {
		for (Character c : characters) {
			if (c.getHandle().equals(handle)) {
				return c;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The "internal" name of the opera, used by SavoyCraft for song addresses
	 * etc.
	 * 
	 * @return
	 */
	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public File getMusicForScene(String sceneHandle) {
		return getResourceStream(sceneHandle + ".mp3");
	}

	/**
	 * 
	 * @param resourceName
	 * @return null if not successful, or not found
	 */
	private File getResourceStream(String resourceName) {
		// System.out.println("Extracting from " + sourceFile.getPath() + ": "
		// + resourceName);
		// return ZipUtil.extract(sourceFile.getPath(), resourceName);
		return new File(sourceFile, resourceName);
	}
}
