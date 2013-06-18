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
package com.savoycraft.opera.editor;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.savoycraft.vixen.VixenChannel;
import com.savoycraft.vixen.VixenSequence;

/**
 *
 */
public class VixenBeatTrackConverter {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		FileDialog fd = new FileDialog((Frame) null);
		fd.show();
		File vixenFile = new File(fd.getDirectory() + fd.getFile());

		VixenSequence seq = new VixenSequence(vixenFile);

		String[] channelNames = new String[seq.getChannels().size()];
		for (int i = 0; i < seq.getChannels().size(); i++) {
			channelNames[i] = seq.getChannel(i).getName();
		}

		int choice = JOptionPane.showOptionDialog(null, "Choose Channel",
				"Choose Channel", JOptionPane.OK_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, channelNames,
				channelNames[0]);
		if (choice < 0) {
			return;
		}

		File outputFile = new File("C:\\users\\bj\\desktop\\beat.txt");
		outputFile.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));

		VixenChannel channel = seq.getChannel(choice);
		LinkedList<Integer> eventList = seq.convertEventPeriodsToMs(channel
				.getEventList());
		for (Integer i : eventList) {
			Double secTime = i.doubleValue() / 1000d;
			out.write(secTime + ",");
			System.out.println (secTime);
		}
		
		System.out.println ("Done!");

		out.flush();
		out.close();

		System.exit(0);
	}

}
