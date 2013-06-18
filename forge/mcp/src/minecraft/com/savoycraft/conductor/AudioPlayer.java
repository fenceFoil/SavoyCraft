/**
 * 
 */
package com.savoycraft.conductor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author BJ
 * 
 */
public class AudioPlayer extends Thread {

	private AudioInputStream audioIn;
	private SourceDataLine speakers;

	private boolean playing = false;

	private boolean stop = false;

	private int samplePosition = 0;

	private static int SAMPLE_RATE = 44100;

	private AudioFormat outFormat = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED, SAMPLE_RATE, 16, 2, 4, 44100, true);

	public AudioPlayer(byte[] data) {
		setUp(new ByteArrayInputStream(data));
	}

	public AudioPlayer(InputStream in) {
		setUp(in);
	}

	public AudioPlayer(File audioFile) {
		setUp(audioFile);
	}

	private void setUp(InputStream in) {
		try {
			AudioInputStream rawIn = AudioSystem.getAudioInputStream(in);
			AudioFormat baseFormat = rawIn.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			outFormat = decodedFormat;
			audioIn = AudioSystem.getAudioInputStream(decodedFormat, rawIn);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUp(File audioFile) {
		try {
			AudioInputStream rawIn = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat baseFormat = rawIn.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			outFormat = decodedFormat;
			audioIn = AudioSystem.getAudioInputStream(decodedFormat, rawIn);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startAudio() {
		try {
			AudioFormat inFormat = audioIn.getFormat();
			// outFormat = new AudioFormat(inFormat.getSampleRate(),
			// inFormat.getSampleSizeInBits(), inFormat.getChannels(),
			// false, inFormat.isBigEndian());
			speakers = AudioSystem.getSourceDataLine(outFormat);
			speakers.open(outFormat);
			speakers.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopAudio() {
		speakers.drain();
		speakers.stop();
		speakers.close();
		try {
			audioIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		startAudio();

		// Buffer is sized to hold ~1/40 secs of audio
		byte[] audioBuffer = new byte[1024];

		playing = true;

		while (!stop) {
			int read;
			try {
				read = audioIn.read(audioBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}

			samplePosition += read / audioIn.getFormat().getFrameSize();

			if (read < audioBuffer.length) {
				speakers.write(audioBuffer, 0, read);
				break;
			} else {
				speakers.write(audioBuffer, 0, audioBuffer.length);
			}

			//System.out.println(samplesToSeconds(samplePosition));
		}

		playing = false;
		stop = false;

		stopAudio();
	}

	private double samplesToSeconds(int samples) {
		return (double) samples / (double) SAMPLE_RATE;
	}

	private int secondsToSamples(double seconds) {
		return (int) ((double) SAMPLE_RATE * seconds);
	}

	/**
	 * Gets position currently playing in song
	 * 
	 * @return in seconds
	 */
	public double getPosition() {
		return samplesToSeconds(samplePosition);
	}

	public static void main(String args[]) {
		AudioPlayer c = new AudioPlayer(
				new File(
						"C:/Users/BJ/Music/Princess Ida 1932/Split/We Are Warriors Three.mp3"));
		c.start();
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public void stopPlayer() {
		stop = true;
	}
}
