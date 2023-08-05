package ca.ucalgary.edu.ensf380;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TrainMapCreator {

    public static void createImage(int width, int height, ArrayList<Integer> xCoordinates, ArrayList<Integer> yCoordinates, int currentTrain) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        
        for (int i = 0; i < xCoordinates.size(); i++) {
			if(i != currentTrain) {
				graphics.setColor(Color.BLACK);
			} else {
				graphics.setColor(Color.RED);
			}
            graphics.fillOval(xCoordinates.get(i), yCoordinates.get(i), 20, 20);
        }

        try {
            File outputImageFile = new File("data", "trainmap.png");
            ImageIO.write(image, "png", outputImageFile);
            System.out.println("Image file created successfully: ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();
    }
}