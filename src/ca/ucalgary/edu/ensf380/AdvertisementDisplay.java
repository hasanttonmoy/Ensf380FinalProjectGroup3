package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdvertisementDisplay extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Advertisement> advertisements;
	private int currentAdIndex = 0;

	private JLabel titleLabel;
	private JLabel subtitleLabel;
	private JLabel textLabel;
	private JLabel mediaLabel;

	public AdvertisementDisplay(List<Advertisement> advertisements) {
		this.advertisements = advertisements;

		// Create labels
		titleLabel = new JLabel();
		subtitleLabel = new JLabel();
		textLabel = new JLabel();
		mediaLabel = new JLabel();

		// Create panel and add labels
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(titleLabel);
		panel.add(subtitleLabel);
		panel.add(textLabel);
		panel.add(mediaLabel);

		// Add panel to frame
		add(panel);

		// Set frame properties
		setTitle("Advertisements");
		setSize(400, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		// Set timer to rotate advertisements
		Timer timer = new Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayNextAdvertisement();
			}
		});
		timer.start();

		// Display the first advertisement
		displayNextAdvertisement();
	}

	private void displayNextAdvertisement() {
		Advertisement ad = advertisements.get(currentAdIndex);
		titleLabel.setText("Title: " + ad.getTitle());
		subtitleLabel.setText("Subtitle: " + ad.getSubtitle());
		textLabel.setText("Text: " + ad.getText());

		// Load image from media path
		// Load image from media path
		ImageIcon icon = new ImageIcon(ad.getMediaPath());

		// Scale the image to fit the label
		int imageWidth = 200; // Desired width
		int imageHeight = 150; // Desired height
		Image image = icon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);

		// Set the scaled image to the label
		mediaLabel.setIcon(new ImageIcon(image));

		// Increment index for next advertisement
		currentAdIndex = (currentAdIndex + 1) % advertisements.size();
	}

}
