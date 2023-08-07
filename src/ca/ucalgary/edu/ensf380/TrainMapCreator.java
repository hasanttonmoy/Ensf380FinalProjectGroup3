package ca.ucalgary.edu.ensf380;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * TrainMapCreator is used to generate an image of the trains at each station.
 * The class requires x and y coordinates of each train and the number of the curretn train to generate the image.
 * 
 * @author Owen Guldberg
 * @version 2023-08-6
 */
public class TrainMapCreator {

	/**
	 * Creates a train map image with the provided coordinates
	 * 
	 * @param xCoordinates
	 * @param yCoordinates
	 * @param currentTrain
	 */
	public static void createImage(ArrayList<Integer> xCoordinates, ArrayList<Integer> yCoordinates, int currentTrain) {
		try {
			// Gets the train map image for the train lines
			String currentDirectory = System.getProperty("user.dir");
			String backgroundPath = currentDirectory + File.separator + "data\\Trains.png";
			BufferedImage background = ImageIO.read(new File(backgroundPath));

			// Get the width and height of the background image for later
			int width = background.getWidth();
			int height = background.getHeight();

			// Create the new image
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = image.createGraphics();

			// Set background to white
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, width, height);
			graphics.drawImage(background, 0, 0, null);

			// Draws the train positions on the map, black for all trains except the current
			// train which is orange
			for (int i = 0; i < xCoordinates.size(); i++) {
				if (i != currentTrain) {
					graphics.setColor(Color.BLACK);
				} else {
					graphics.setColor(Color.ORANGE);
				}
				double xVal = (double) xCoordinates.get(i) / 2.65;
				double yVal = (double) yCoordinates.get(i) / 1.52;
				graphics.fillOval((int) xVal, (int) yVal, 25, 25);
			}

			// Save the generated image to a file in the data folder
			try {
				File outputImageFile = new File("data", "trainmap.png");
				ImageIO.write(image, "png", outputImageFile);
				System.out.println("Image file created successfully");

			} catch (IOException e) {
				e.printStackTrace();
			}

			// Clean up graphics resources
			graphics.dispose();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}