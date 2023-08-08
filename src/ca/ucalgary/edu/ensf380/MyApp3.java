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
import java.util.ArrayList;

public class MyApp3 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String NO_ARTICLE = "No articles found.";
    private JButton startButton;
    private JButton stopButton;
    private Process process;
    private ExecutorService executor;
    
    private String currentNewsText;
    String query = "Calgary";

    public MyApp3() {
        setTitle("Subway Screen");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopProcess();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension(200, 38));
        buttonPanel.add(startButton);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);
        stopButton.setPreferredSize(new Dimension(200, 38));
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.SOUTH);
        
        
        NewsFetcher.Article article = NewsFetcher.fetchNews(query, "5-8tIA1Lcsf0C4UoBn18EL-dEpRpc8GXogoACHGpnkA");
        if (article != null) {
            currentNewsText = article.getTitle() + " " + article.getSummary();
            startNewsTicker(currentNewsText);
        } else {
            currentNewsText = NO_ARTICLE;
            startNewsTicker(currentNewsText);
        }
		
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        executor = Executors.newFixedThreadPool(2);
    }
    /**
     * startNewsTicker displays a news ticker on the gui based on the input string
     * 
     * @param newsText
     */
    public void startNewsTicker(String newsText) {
        JPanel newsPanel = new JPanel(new BorderLayout()); // Create a panel to hold the news ticker
        newsPanel.setPreferredSize(new Dimension(getWidth(), 30)); // Set the preferred height

        JLabel newsLabel = new JLabel(newsText, SwingConstants.LEFT); // Set the text alignment to left
        newsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        newsPanel.add(newsLabel, BorderLayout.CENTER);

        add(newsPanel, BorderLayout.NORTH);

        if (!newsText.equals(NO_ARTICLE)) {
            Timer timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	// Simulates scrolling by moving last char of string to the back repeatedly
                    String labelText = newsLabel.getText();
                    char firstChar = labelText.charAt(0);
                    labelText = labelText.substring(1) + firstChar;
                    newsLabel.setText(labelText);

                    if (labelText.equals(currentNewsText)) {
                        // Scrolling has reached the end so fetch a new article
                        NewsFetcher.Article newArticle = NewsFetcher.fetchNews(query, "5-8tIA1Lcsf0C4UoBn18EL-dEpRpc8GXogoACHGpnkA");
                        if (newArticle != null) {
                            currentNewsText = newArticle.getTitle() + " " + newArticle.getSummary();
                            newsLabel.setText(currentNewsText);
                        }
                    }
                }
            });
            timer.start();
        }
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
				ProcessBuilder builder = new ProcessBuilder("java", "-jar", "./exe/SubwaySimulator.jar", "--in",
						"./data/subway.csv", "--out", "./out");
				builder.redirectErrorStream(true);
				process = builder.start();

				executor.execute(() -> {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

						String line;
						int i = 0;
						int currentTrain = 3; // 1-12
						while ((line = reader.readLine()) != null) {
							i++;
							if (4 == i) {

								TrainArray.Train[] trains = TrainArray.parseCsvFile();

								System.out.println(trains[currentTrain].toString());

								ArrayList<Integer> xCoordinates = new ArrayList<>();
								ArrayList<Integer> yCoordinates = new ArrayList<>();
								for (TrainArray.Train train : trains) {
									xCoordinates.add(train.getTrainXCord());
									yCoordinates.add(train.getTrainYCord());
								}

								TrainMapCreator.createImage(xCoordinates, yCoordinates, currentTrain);



								i = 0;
							}
							

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
