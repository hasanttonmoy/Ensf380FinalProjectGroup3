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
import java.util.List;
/**
 * MyApp3 is the main GUI class that is used to display our GUI
 */
public class MyApp3 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String NO_ARTICLE = "No articles found.";
    private JButton startButton;
    private JButton stopButton;
    private Process process;
    private ExecutorService executor;
    
    private String currentNewsText;
    private static String query;
    private static int currentTrain;
    private static int cityCode;
    private static String userAPI = "";
    
    private JPanel newsPanel;
    private JLabel textLabel = new JLabel("");
    
	private AdvertisementDisplay advertisementDisplay;
    private WeatherReportDisplay WeatherReportDisplay;
    
    private static List<Advertisement> advertisements;
    
    private TrainArray.Train[] trains;

    public MyApp3() {
        setTitle("Subway Screen");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopProcess();
                dispose();
            }
        });
        // Start Button
        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension(200, 38));
        buttonPanel.add(startButton);
        
        // Stop Button
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);
        stopButton.setPreferredSize(new Dimension(200, 38));
        buttonPanel.add(stopButton);
        
        advertisementDisplay = new AdvertisementDisplay(advertisements);
        advertisementDisplay.setVisible(false); // Initially hidden

        // Create Weather Report GUI
        WeatherReportDisplay = new WeatherReportDisplay(cityCode);
        WeatherReportDisplay.setVisible(false); // Initially hidden

        
        add(advertisementDisplay, BorderLayout.WEST);
        add(WeatherReportDisplay, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.NORTH);
        
        // Uses news api either with user inputted api or a default one
        if("" != userAPI) {  
	        NewsFetcher.Article article = NewsFetcher.fetchNews(query, userAPI);
	        if (article != null) {
	            currentNewsText = article.getTitle() + " " + article.getSummary();
	            startNewsTicker(currentNewsText);
	        } else {
	            currentNewsText = NO_ARTICLE;
	            startNewsTicker(currentNewsText);
	        }
        } else {
	        NewsFetcher.Article article = NewsFetcher.fetchNews(query);
	        if (article != null) {
	            currentNewsText = article.getTitle() + " " + article.getSummary();
	            startNewsTicker(currentNewsText);
	        } else {
	            currentNewsText = NO_ARTICLE;
	            startNewsTicker(currentNewsText);
	        }
        }
        
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
    	 // Formatting
    	
        newsPanel = new JPanel(new BorderLayout());
        newsPanel.setPreferredSize(new Dimension(getWidth(), 80));
        newsPanel.setBackground(Color.GREEN);
        JLabel newsLabel = new JLabel(newsText, SwingConstants.LEFT);
        newsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        newsPanel.add(newsLabel, BorderLayout.CENTER);
        add(newsPanel, BorderLayout.SOUTH);
        newsPanel.setVisible(false);

        // Only scroll text if article was found
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
				startDisplay();
				
				ProcessBuilder builder = new ProcessBuilder("java", "-jar", "./exe/SubwaySimulator.jar", "--in",
						"./data/subway.csv", "--out", "./out");
				builder.redirectErrorStream(true);
				process = builder.start();

				executor.execute(() -> {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
						String line;
						int i = 0;
						
						while ((line = reader.readLine()) != null) {
							i++;
							if (4 == i) {
								//Update train values
								trains = TrainArray.parseCsvFile();
								//Update map
								ArrayList<Integer> xCoordinates = new ArrayList<>();
								ArrayList<Integer> yCoordinates = new ArrayList<>();
								
								for (TrainArray.Train train : trains) {
									xCoordinates.add(train.getTrainXCord());
									yCoordinates.add(train.getTrainYCord());
								}
								TrainMapCreator.createImage(xCoordinates, yCoordinates, currentTrain);
								//Update stations that are being shown
						        textLabel.setText(trains[currentTrain].getPrevStationName() +
						        		" -->  |" + trains[currentTrain].getCurrentStationName() + "|  ---> " +
						        		trains[currentTrain].getNextStationName());
						        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
						        newsPanel.add(textLabel, BorderLayout.NORTH);
								
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
			stopDisplay();
			process.destroy();
			process = null;
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
		}
	}
	
    private void startDisplay() {
        advertisementDisplay.setVisible(true);
        WeatherReportDisplay.setVisible(true);
        newsPanel.setVisible(true);
        
    }

    private void stopDisplay() {
        advertisementDisplay.setVisible(false);
        WeatherReportDisplay.setVisible(false);
        newsPanel.setVisible(false);
    }

	public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.createConnection();
        AdvertisementManager manager = new AdvertisementManager();
        manager.loadAdvertisementsFromDatabase(dbConnection);

        if (args.length > 0) {
            try {
                int trainNum = Integer.parseInt(args[0]);
                if (trainNum >= 1 && trainNum <= 12) {
                    currentTrain = trainNum - 1;
                } else {
                    System.err.println("Invalid train number provided. Using default value.");
                    currentTrain = 0; // Default
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid city code format. Using default value.");
                currentTrain = 0; // Default
            }
        } else {
        	currentTrain = 0; // Default
        }
		
		
		if (args.length > 1) { 
			query = args[1];
		} else {
			query = "Calgary"; // Default
		}
		
		if (args.length > 2) {
			cityCode = Integer.parseInt(args[2]);
		} else {
			cityCode = 5913490; // Default
		}
		
		if (args.length > 3) {
			userAPI = args[3];
		}
		
		advertisements = manager.getAdvertisements();
		SwingUtilities.invokeLater(MyApp3::new);
		
        dbConnection.close();
	}
}
