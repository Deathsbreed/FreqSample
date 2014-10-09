package freqsample;

import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;

/**
 * @author Nicolás A. Ortega
 * @copyright Nicolás A. Ortega
 * @license GNU GPLv3
 * @year 2014
 * 
 */
public class FreqSample {
	private JFrame frame;
	private JPanel panel;
	private JTextField hzField, msField;
	private JLabel hzLabel, msLabel;
	private JButton playButton;

	public FreqSample() {
		frame = new JFrame("FreqSample");
		frame.setSize(400, 160);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		frame.add(panel);

		panel.setLayout(null);

		hzLabel = new JLabel("Frequency (Hz):");
		hzLabel.setBounds(10, 10, 150, 25);
		panel.add(hzLabel);
		hzField = new JTextField();
		hzField.setBounds(170, 10, 220, 25);
		panel.add(hzField);

		msLabel = new JLabel("Duration (ms):");
		msLabel.setBounds(10, 40, 150, 25);
		panel.add(msLabel);
		msField = new JTextField();
		msField.setBounds(170, 40, 220, 25);
		panel.add(msField);

		playButton = new JButton("Play");
		playButton.setBounds(160, 80, 80, 25);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				double hz = Double.parseDouble(hzField.getText());
				double ms = Double.parseDouble(msField.getText());
				try {
					Generator.generateTone(hz, ms);
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				} catch(LineUnavailableException lue) {
					lue.printStackTrace();
				}
			}
		});
		panel.add(playButton);

		frame.setVisible(true);
	}

	public static void main(String[] args) { new FreqSample(); }
}
