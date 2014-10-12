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
	private static final String version = "v1.0.2";
	private JFrame frame;
	private JPanel panel;
	private JTextField hzField, msField;
	private JLabel hzLabel, msLabel;
	private JButton playButton;

	private JMenuBar menuBar;
	private JMenu fsMenu, helpMenu;
	private JMenuItem quitItem, copyrightItem, aboutItem;

	public FreqSample() {
		frame = new JFrame("FreqSample");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		frame.add(panel);

		setupMenu();
		setupUI();

		frame.setVisible(true);
	}

	private void setupMenu() {
		menuBar = new JMenuBar();
		fsMenu = new JMenu("FreqSample");
		helpMenu = new JMenu("Help");

		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		fsMenu.add(quitItem);

		copyrightItem = new JMenuItem("Copyright Info");
		copyrightItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				new Window("Copyright Information",
					"FreqSample " + version + ", play frequencies.\n" +
					"Copyright (C) 2014 Nicolás A. Ortega\n\n" +
					"This program is free software: you can redistribute it and/or modify\n" +
					"it under the terms of the GNU General Public License as published by\n" +
					"the Free Software Foundation, either version 3 of the License, or\n" +
					"(at your option) any later version.\n" +
					"This program is distributed in the hope that it will be useful,\n" +
					"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
					"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
					"GNU General Public License for more details.\n\n" +
					"You should have received a copy of the GNU General Public License\n" +
					"along with this program.  If not, see <http://www.gnu.org/licenses/>.\n\n");
			}
		});
		helpMenu.add(copyrightItem);

		aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				new Window("About",
					"FreqSample " + version + "\n" +
					"Copyright (C) 2014 Nicolás A. Ortega\n" +
					"License: GNU GPLv3\n" +
					"Contact: nicolas.ortega.froysa@gmail.com\n" +
					"Source-Code: https://github.com/Deathsbreed/FreqSample\n" +
					"Developers: Nicolás Ortega\n\n");
			}
		});
		helpMenu.add(aboutItem);

		menuBar.add(fsMenu);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
	}

	private void setupUI() {
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
			@Override
			public void actionPerformed(ActionEvent ae) {
                                double hz = Double.parseDouble(hzField.getText());
                                double ms = Double.parseDouble(msField.getText());
                                try {
                                        Generator.generateTone(hz, ms);
                                } catch(InterruptedException ie) {
                                        ie.printStackTrace();
					System.exit(1);
                                } catch(LineUnavailableException lue) {
                                        lue.printStackTrace();
					System.exit(1);
                                }
                        }
                });
                panel.add(playButton);
	}

	public class Window {
		public Window(String title, String msg) {
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void main(String[] args) { new FreqSample(); }
}
