package library;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PlaySound implements Runnable {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) {

		PlaySound im = new PlaySound("/home/sephiroth/fond3.ogg");
		Thread t1 = new Thread(im);
		t1.start();
	}

	private final String	file;

	public PlaySound(final String file) {

		this.file = file;
	}

	private SourceDataLine getLine(AudioFormat audioFormat)
					throws LineUnavailableException {

		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
						audioFormat);
		System.out.println(info);
		res = (SourceDataLine) AudioSystem.getLine(info);
		System.out.println(audioFormat);
		System.out.println(res);
		res.open(audioFormat);
		return res;
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din)
					throws IOException, LineUnavailableException {

		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	public void run() {

		try {
			File file = new File(this.file);
			// Get AudioInputStream from given file.
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			if (in != null) {
				AudioFormat baseFormat = in.getFormat();
				System.out.println(baseFormat);
				AudioFormat decodedFormat = new AudioFormat(
								AudioFormat.Encoding.PCM_SIGNED,
								baseFormat.getSampleRate(), 16,
								baseFormat.getChannels(),
								baseFormat.getChannels() * 2,
								baseFormat.getSampleRate(), false);
				// Get AudioInputStream that will be decoded by underlying
				// VorbisSPI
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				System.out.println(din);
				// Play now !
				rawplay(decodedFormat, din);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}