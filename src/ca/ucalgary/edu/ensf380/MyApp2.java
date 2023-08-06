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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author  Mahdi Ansari
 *
 */
public class MyApp2 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
    private JTextArea outputTextArea;
    private JButton stopButton;
    private Process process;

    public MyApp2() {
        super("Subway Screen");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Create the text area to display the output
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        // Create the stop button
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);

        // Add the text area and button to the content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(stopButton, BorderLayout.SOUTH);
        setContentPane(contentPane);
        
        // Set the size and visibility of the frame
        setSize(600, 400);
        setVisible(true);
        
        // Launch the executable jar file
        try {
        	String[] command = {"java", "-jar", "./exe/SubwaySimulator.jar", "--in", "./data/subway.csv", "--out", "./out"};
        	process = new ProcessBuilder(command).start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
            	System.out.println(line);
                outputTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Add a window listener to stop the process when the window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (process != null) {
                    process.destroy();
                }
                dispose();
            }
        });

        // Add a shutdown hook to stop the process when the application is closed
        final Process finalProcess = process;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalProcess != null) {
                finalProcess.destroy();
            }
            System.exit(0);
        }));        
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stopButton) {
            process.destroy();
            stopButton.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        new MyApp2();
    }
}
