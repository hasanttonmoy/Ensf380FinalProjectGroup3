package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SubwayScreen extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AdvertisementDisplay advertisementDisplay;
    private WeatherReportDisplay WeatherReportDisplay;
    private JButton startButton;
    private JButton stopButton;

    public SubwayScreen(List<Advertisement> advertisements, int cityCode) {
    	
    	
        // Create Advertisement Display
        advertisementDisplay = new AdvertisementDisplay(advertisements);
        advertisementDisplay.setVisible(false); // Initially hidden

        // Create Weather Report GUI
        WeatherReportDisplay = new WeatherReportDisplay(cityCode);
        WeatherReportDisplay.setVisible(false); // Initially hidden

        // Create Start and Stop buttons
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        startButton.addActionListener(e -> startDisplay());
        stopButton.addActionListener(e -> stopDisplay());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        // Set Layout
        setLayout(new BorderLayout());

        // Add Advertisement and Weather Panels
        add(advertisementDisplay, BorderLayout.WEST);
        add(WeatherReportDisplay, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel at the bottom

        // Set frame properties
        setTitle("Subway Screen");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startDisplay() {
        advertisementDisplay.setVisible(true);
        WeatherReportDisplay.setVisible(true);
    }

    private void stopDisplay() {
        advertisementDisplay.setVisible(false);
        WeatherReportDisplay.setVisible(false);
    }

    public static void main(String[] args) {
        // Database Connection and Advertisement Loading
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.createConnection();
        AdvertisementManager manager = new AdvertisementManager();
        manager.loadAdvertisementsFromDatabase(dbConnection);

        // Get city code from command-line argument, or use default value if not provided
        int cityCode = args.length > 0 ? Integer.parseInt(args[0]) : 5913490; // Replace 123456 with your default city code

        // Create and display the Subway Screen
        new SubwayScreen(manager.getAdvertisements(), cityCode);

        dbConnection.close();
    }
}
