/**
 * Copyright (c) 2022-2023 Mahdi Jaberzadeh Ansari and others.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be
 *	included in all copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *	LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *	OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mahdi Ansari
 *
 */
public class MyApp3 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextArea outputArea;
    private JButton startButton;
    private JButton stopButton;
    private Process process;
    private ExecutorService executor;

    public MyApp3() {
        setTitle("Subway Screen 3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopProcess();
                dispose();
            }
        });

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension(100, 38));
        buttonPanel.add(startButton);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);
        stopButton.setPreferredSize(new Dimension(100, 38));
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startProcess();
        } else if (e.getSource() == stopButton) {
            stopProcess();
        }
    }

    private void startProcess() {
        if (process == null) {
            try {
                ProcessBuilder builder = new ProcessBuilder("java", "-jar", "./exe/SubwaySimulator.jar", "--in", "./data/subway.csv", "--out", "./out");
                builder.redirectErrorStream(true);
                process = builder.start();

                executor.execute(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                            outputArea.append(line + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                executor.execute(() -> {
                    try {
                        process.waitFor();
                        process = null;
                        SwingUtilities.invokeLater(() -> {
                            stopButton.setEnabled(false);
                            startButton.setEnabled(true);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                stopButton.setEnabled(true);
                startButton.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopProcess() {
        if (process != null) {
            process.destroy();
            process = null;
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyApp3::new);
    }
}

