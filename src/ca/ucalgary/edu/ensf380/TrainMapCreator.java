package ca.ucalgary.edu.ensf380;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TrainMapCreator {

    public static void createImage(ArrayList<Integer> xCoordinates, ArrayList<Integer> yCoordinates, int currentTrain) {
		try {
			
		String currentDirectory = System.getProperty("user.dir");
		String backgroundPath = currentDirectory + File.separator + "data\\Trains.png";
			
		BufferedImage background = ImageIO.read(new File(backgroundPath));
		int width = background.getWidth();
		int height = background.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
		
		graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
		graphics.drawImage(background, 0, 0, null);

        
        for (int i = 0; i < xCoordinates.size(); i++) {
			if(i != currentTrain) {
				graphics.setColor(Color.BLACK);
			} else {
				graphics.setColor(Color.ORANGE);
			}
			double xVal = (double) xCoordinates.get(i) / 2.65;
			double yVal = (double) yCoordinates.get(i) / 1.52;
            graphics.fillOval((int) xVal, (int) yVal, 25, 25);
        }

        try {
            File outputImageFile = new File("data", "trainmap.png");
            ImageIO.write(image, "png", outputImageFile);
            System.out.println("Image file created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}