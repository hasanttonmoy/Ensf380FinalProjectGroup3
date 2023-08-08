package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WeatherReportDisplay extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea weatherReportArea;

	public WeatherReportDisplay(int cityCode) {
		setLayout(new BorderLayout());

		// Text area to display weather report
		weatherReportArea = new JTextArea();
		weatherReportArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(weatherReportArea);

		add(scrollPane, BorderLayout.CENTER);

		// Fetch and display the weather report for the specified city code
		displayWeatherReport(cityCode);
	}

	private void displayWeatherReport(int cityCode) {
		try {
			List<String> weatherReport = WeatherReport.getWeatherReport(cityCode);

			weatherReportArea.setText(""); // Clear previous report
			for (String line : weatherReport) {
				weatherReportArea.append(line + "\n");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error fetching weather report.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
