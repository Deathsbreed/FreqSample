package freqsample;

import java.nio.ByteBuffer;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * @author Nicolás A. Ortega
 * @copyright Nicolás A. Ortega
 * @license GNU GPLv3
 * @year 2014
 * 
 */
public class Generator {
	public static void generateTone(double hz, double msecs) throws InterruptedException, LineUnavailableException {
		final int SAMPLE_RATE = 44100;
		final int SAMPLE_SIZE = 2;

		SourceDataLine line;
		double fFreq = hz;

		double fCyclePosition = 0;

		// Open Audio Device
		AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);

		if(!AudioSystem.isLineSupported(info)) {
			System.out.println("Line matching " + info + " is not supported.");
			throw new LineUnavailableException();
		}

		line = (SourceDataLine)AudioSystem.getLine(info);
		line.open(af);
		line.start();

		// The audio bytebuffer
		ByteBuffer cBuf = ByteBuffer.allocate(line.getBufferSize());

		// Set the total samples to loop through
		if(msecs < 150) msecs = 150;
		int ctSamplesTotal = (int)(SAMPLE_RATE * (msecs / 1000));

		while(ctSamplesTotal > 0) {
			// Define the frequency at the given sample rate
			double fCycleInc = fFreq/SAMPLE_RATE;

			// Clear the buffer
			cBuf.clear();

			// Play the frequency
			int ctSamplesThisPass = line.available() / SAMPLE_SIZE;
			for(int i = 0; i < ctSamplesThisPass; i++) {
				cBuf.putShort((short)(Short.MAX_VALUE * Math.sin(2 * Math.PI * fCyclePosition)));
				fCyclePosition += fCycleInc;
				if(fCyclePosition > 1) fCyclePosition -= 1;
			}
			line.write(cBuf.array(), 0, cBuf.position());
			ctSamplesTotal -= ctSamplesThisPass;

			while(line.getBufferSize() / 2 < line.available()) {
				Thread.sleep(1);
			}
		}

		// Close the audioline
		line.drain();
		line.close();
	}
}
