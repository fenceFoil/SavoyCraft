package com.savoycraft.opera.editor;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import com.savoycraft.conductor.AudioPlayer;

public class BeatTrackMaker {

	private JFrame frame;
	private AudioPlayer ap;
	private BufferedWriter out;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BeatTrackMaker window = new BeatTrackMaker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public BeatTrackMaker() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		FileDialog fd = new FileDialog((Frame)null);
		fd.show();
		String musicFile = fd.getDirectory() + fd.getFile();
		
		ap = new AudioPlayer(new File(musicFile));
		ap.start();
		
		File outputFile = new File ("C:\\users\\bj\\desktop\\beat.txt");
		outputFile.createNewFile();
		out = new BufferedWriter(new FileWriter(outputFile));
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				onClick();
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				onClick();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void onClick() {
		try {
			out.write(String.format("%.4f,", ap.getPosition()));
			out.flush();
			System.out.println(".");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
