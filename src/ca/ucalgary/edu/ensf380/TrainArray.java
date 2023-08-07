package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/*
 * The TrainArray class is used to create an array of trains that stores data generated from the SubwaySimulator
 * 
 * @author Owen Guldberg
 * @version 2023-08-06
 */
public class TrainArray {
	/*
	 * A train object that stores data generated from the SubwaySimulator
	 */
    public static class Train {
        private String lineName;
        private int trainNumber;
        private String stationCode;
        private String direction;
        private String destination;
		private String nextStation;
		private String prevStation;
		
		private String currentStationName;
		private String nextStationName;
		private String prevStationName;
		private String currentStationXCord;
		private String currentStationYCord;
		
		/**
		 * Creates the train object
		 * 
		 * @param lineName
		 * @param trainNumber
		 * @param stationCode
		 * @param direction
		 * @param destination
		 * @param nextStation
		 * @param prevStation
		 */
        public Train(String lineName, int trainNumber, String stationCode, String direction, String destination, String nextStation, String prevStation) {
            this.lineName = lineName;
            this.trainNumber = trainNumber;
            this.stationCode = stationCode;
            this.direction = direction;
            this.destination = destination;
			this.nextStation = nextStation;
			this.prevStation = prevStation;
			this.currentStationName = GetStationData.getNameByCode(stationCode);
			this.nextStationName = GetStationData.getNameByCode(nextStation);
			this.prevStationName = GetStationData.getNameByCode(prevStation);
			this.currentStationXCord = GetStationData.getXCordByCode(stationCode);
			this.currentStationYCord = GetStationData.getYCordByCode(stationCode);
			
        }
        
        // Getters
        public String getLineName() {
            return lineName;
        }
        public int getTrainNumber() {
            return trainNumber;
        }
        public String getStationCode() {
            return stationCode;
        }
        public String getDirection() {
            return direction;
        }
        public String getDestination() {
            return destination;
        }
		public int getTrainXCord() {
			return (int) Float.parseFloat(currentStationXCord);
		}
		public int getTrainYCord() {
			return (int) Float.parseFloat(currentStationYCord);
		}

        @Override
        public String toString() { // Used for debugging
            return "Train{" +
                    "lineName='" + lineName + '\'' +
                    ", trainNumber=" + trainNumber +
                    ", stationCode='" + stationCode + '\'' +
                    ", direction='" + direction + '\'' +
                    ", destination='" + destination + '\'' +
					", nextStation='" + nextStation + '\'' +
					", prevStation='" + prevStation + '\'' +
					", currentStationName='" + currentStationName + '\'' +
					", nextStationName ='" + nextStationName + '\'' +
					", prevStationName='" + prevStationName + '\'' +
					", currentStationXCord='" + currentStationXCord + '\'' +
					", currentStationYCord='" + currentStationYCord + '\'' +
                    '}';
        }
    }
    
    /**
     * This method is used to parse the SubwaySimulator csv file output and then create an array of train objects from the data
     * @return An array of train objects that stores the data
     */
	public static Train[] parseCsvFile() {
		// Create the array list
		List<Train> trains = new ArrayList<>();
		
		// Read the most recently created csv file that is in the out folder
		String currentDirectory = System.getProperty("user.dir");
		String outputPath = currentDirectory + File.separator + "out";
		File newestfile = getLastModified(outputPath);
		String lastpath = outputPath + "\\" + newestfile.getName();
		try (BufferedReader br = new BufferedReader(new FileReader(lastpath))) {
			String line;
			
			// Skip the header
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
					continue;
				}
				
				// Delimiter
				String[] data = line.split(",");
				if (data.length == 5) {
					
					// Save all the data for a train object
					String lineName = data[0].trim();
					int trainNumber = Integer.parseInt(data[1].trim());
					String stationCode = data[2].trim();
					String direction = data[3].trim();
					String destination = data[4].trim();
					String input = stationCode;
					
					// Get the station code of the prev and next train station depending on train direction.
					// Also split the station code to get "R" and an int for the station number
					String regex = "(\\d+)$";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(input);
					String numberStr = "";
					if (matcher.find()) {
						numberStr = matcher.group();
					}
					int nextCode = Integer.parseInt(numberStr);
					if("forward" == direction) {
						nextCode++;
						if(nextCode > 120) {
							nextCode = 1;
						}
					} else {
						nextCode--;
						if(nextCode < 1) {
							nextCode = 120;
						}
					}
					int prevCode = 0;
					if("forward" == direction) {
						prevCode = nextCode - 2;
					} else {
						prevCode = nextCode + 2;
					}
					if(prevCode > 120) {
						prevCode = 1;
					}
					if(prevCode < 1) {
						prevCode = 120;
					}
					
					// Turn the station values back into Strings
					String nextStation = input.replaceAll(regex, String.format("%02d", nextCode));
					String prevStation = input.replaceAll(regex, String.format("%02d", prevCode));
					
					// Create the train object and add it to the list
					Train train = new Train(lineName, trainNumber, stationCode, direction, destination, nextStation, prevStation);
					trains.add(train);
				}
			}
			
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
		return trains.toArray(new Train[0]);
	}
	
	/**
	 * Finds the path of the most recently created file in a directory
	 * 
	 * @param directoryFilePath
	 * @return The path of the newest file
	 */
	public static File getLastModified(String directoryFilePath) {

	    File directory = new File(directoryFilePath);
	    // Create array of all the files in the directory
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE; // Represents how long ago the file was created
	    File chosenFile = null; // file object for the newest file
	    
	    // Check that directory was not empty
	    if (null != files) {
	    		// Finds the newest file
				for (File file : files) {
					if (file.lastModified() > lastModifiedTime) {
						chosenFile = file;
						lastModifiedTime = file.lastModified();
					}
				}
			}
			return chosenFile;
		}

    public static void main(String[] args) {
    }
}
