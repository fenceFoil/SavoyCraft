/**
 * 
 * Copyright (c) 2012 William Karnavas All Rights Reserved
 * 
 */

/**
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
package com.savoycraft.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains various methods that simplify working with org.w3c.dom classes
 */
public class DOMUtil {
	/**
	 * Returns a list (possibly empty) with any Elements in the list of nodes
	 * with the given name (case sensitive version).
	 * 
	 * @param name
	 * @param nodes
	 * @return a list; never null
	 */
	public static LinkedList<Element> findElements(String name, NodeList nodes) {
		LinkedList<Element> found = new LinkedList<Element>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
					&& n.getNodeName().equals(name)) {
				found.add((Element) n);
			}
		}
		return found;
	}

	/**
	 * Returns the first Element in the list of nodes with the given name (case
	 * sensitive version).
	 * 
	 * @param name
	 * @param nodes
	 * @return either an Element or null
	 */
	public static Element findElement(String name, NodeList nodes) {
		Element found = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
					&& n.getNodeName().equals(name)) {
				found = (Element) n;
				break;
			}
		}
		return found;
	}

	/**
	 * Returns a list (possibly empty) with any Elements in the list of nodes
	 * with the given name (case insensitive).
	 * 
	 * @param name
	 * @param nodes
	 * @return a list; never null
	 */
	public static LinkedList<Element> findElementsIgnoreCase(String name,
			NodeList nodes) {
		LinkedList<Element> found = new LinkedList<Element>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
					&& n.getNodeName().equalsIgnoreCase(name)) {
				found.add((Element) n);
			}
		}
		return found;
	}

	/**
	 * TODO: Case sensitive?
	 * 
	 * @param element
	 * @param attrName
	 * @return null if not found
	 */
	public static String getAttributeValue(Element element, String attrName) {
		NamedNodeMap attributes = element.getAttributes();
		Node attribute = attributes.getNamedItem(attrName);
		if (attribute == null) {
			return null;
		} else {
			return attribute.getNodeValue();
		}
	}

	/**
	 * As getAttributeValue, except that if there is no attribute with the given
	 * name, this method returns defaultValue instead.
	 * 
	 * @param element
	 * @param attrName
	 * @param defaultValue
	 * @return
	 */
	public static String getAttributeValue(Element element, String attrName,
			String defaultValue) {
		String attrValue = getAttributeValue(element, attrName);
		if (attrValue == null) {
			return defaultValue;
		} else {
			return attrValue;
		}
	}

	/**
	 * Will attempt to find the first element matching the given criteria in the
	 * first level of nodeList.<br>
	 * Case insensitive.
	 * 
	 * @param nodeList
	 * @param elementName
	 * @param attributeName
	 * @param attributeValue
	 * @return null if faliure
	 */
	public static Node findFirstElementWithAttribute(NodeList nodeList,
			String elementName, String attributeName, String attributeValue) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (n instanceof Element) {
				if (n.getNodeName().equalsIgnoreCase(elementName)) {
					// Matching element found. Now search for attribute
					NamedNodeMap attributes = n.getAttributes();
					Node attribute = attributes.getNamedItem(attributeName);
					if (attribute == null) {
						continue;
					} else {
						if (attribute.getNodeValue().equalsIgnoreCase(
								attributeValue)) {
							return n;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Attempts to parse an integer from the given string.
	 * 
	 * @param intString
	 * @return null if parsing fails
	 */
	public static Integer parseInt(String intString) {
		if (intString == null) {
			return null;
		}

		if (intString.matches("[-]?\\d+")) {
			return Integer.parseInt(intString);
		} else {
			return null;
		}
	}

	/**
	 * If the string is "true", return true. If "false", return false. Otherwise
	 * null.
	 * 
	 * @param boolString
	 * @return
	 */
	public static Boolean parseBoolean(String boolString) {
		if (boolString == null) {
			return null;
		}

		if (boolString.toLowerCase().equals("true")) {
			return true;
		} else if (boolString.toLowerCase().equals("false")) {
			return false;
		} else {
			return null;
		}
	}

	/**
	 * As parseIntString, but returns defaultInt in those situations where
	 * parseIntString would fail and return null.
	 * 
	 * @param intString
	 * @param defaultInt
	 * @return the parsed int or defaultInt if parsing fails. Never null.
	 */
	public static Integer parseInt(String intString, Integer defaultInt) {
		Integer returnInt = parseInt(intString);
		if (returnInt == null) {
			return defaultInt;
		} else {
			return returnInt;
		}
	}

	public static Boolean parseBoolean(String boolString, Boolean defaultBool) {
		Boolean returnBool = parseBoolean(boolString);
		if (returnBool == null) {
			return defaultBool;
		} else {
			return returnBool;
		}
	}

	public static Document loadDocument(File f) throws FileNotFoundException {
		// Create the document builder
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		// Parse
		FileInputStream fileIn = new FileInputStream(f);
		Document document = null;
		try {
			document = documentBuilder.parse(fileIn);
			fileIn.close();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document;

	}

	public static Document loadDocument(byte[] bytes) {
		// Create the document builder
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		// Parse
		Document document = null;
		try {
			document = documentBuilder.parse(new ByteArrayInputStream(bytes));
			document.normalizeDocument();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * If first node found is not an element, returns null.
	 * @param d
	 * @return the root element, or null if d is null or a root element is not
	 *         found
	 */
	public static Element getRootElement(Document d) {
		if (d == null) {
			return null;
		}

		NodeList list = d.getChildNodes();
		if (list.getLength() <= 0) {
			return null;
		} else {
			Node n = list.item(0);
			if (n instanceof Element) {
				return (Element) n;
			} else {
				return null;
			}
		}
	}
}
