package freqsample;

import java.nio.ByteBuffer;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * @author Nicolás A. Ortega
 * @copyright Nicolás A. Ortega
 * @license GPLv3
 * @year 2014
 * 
 */
public class FreqSample {
	private static final String version = "v0.1";

	public FreqSample(double hz, double msecs) throws InterruptedException, LineUnavailableException {
		final int SAMPLE_RATE = 44100;
		final int SAMPLE_SIZE = 2;

		SourceDataLine line;
		double fFreq = hz;

		double fCyclePosition = 0;

		AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);

		if(!AudioSystem.isLineSupported(info)) {
			System.out.println("Line matching " + info + " is not supported.");
			throw new LineUnavailableException();
		}

		line = (SourceDataLine)AudioSystem.getLine(info);
		line.open(af);
		line.start();

		ByteBuffer cBuf = ByteBuffer.allocate(line.getBufferSize());

		int ctSamplesTotal = SAMPLE_RATE * (int)(msecs / 1000);

		while(ctSamplesTotal > 0) {
			double fCycleInc = fFreq/SAMPLE_RATE;

			cBuf.clear();

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

		line.drain();
		line.close();
	}

	public static void main(String[] args) throws InterruptedException, LineUnavailableException {
		if(args.length <= 2 && args.length != 0) {
			if(args.length == 1) {
				if(args[0].equals("-h")) printHelp();
				else if(args[0].equals("-c")) printCopyright();
				else if(args[0].equals("-v")) System.out.println(version);
				else printHelp();
			} else {
				double hz = Double.parseDouble(args[0]);
				double ms = Double.parseDouble(args[1]);
				new FreqSample(hz, ms);
			}
		} else {
			printHelp();
		}
	}

	private static void printHelp() {
		System.out.println("Usage: java -jar FreqSample.jar [option][<hz> <ms>]");
		System.out.println(" -h -- Print this help information");
		System.out.println(" -c -- Print copyright information");
		System.out.println(" -v -- Print version\n");
	}

	private static void printCopyright() {
		System.out.println("FreqSample, a simple frequency sampler.\n" +
			"Copyright (C) 2014 Nicolás A. Ortega\n" +
			"This program is free software: you can redistribute it and/or modify\n" +
			"it under the terms of the GNU General Public License as published by\n" +
			"the Free Software Foundation, either version 3 of the License, or\n" +
			"(at your option) any later version.\n\n" +
			"This program is distributed in the hope that it will be useful,\n" +
			"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n" +
			"GNU General Public License for more details.");
	}
}
