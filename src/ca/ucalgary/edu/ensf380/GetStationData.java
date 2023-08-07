package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;


/**
 * The GetStationData class is used to get missing data for train objects that was not generated by the SubwaySimulator.
 * This includes station names, station x-coordinates, and station y-coordinates.
 * 
 * @author Owen Guldberg
 * @version 2023-08-06
 */
public class GetStationData {
	
	// Create maps that will all use station codes as a key
    private static Map<String, String> stationData = new HashMap<>();
	private static Map<String, String> stationXCord = new HashMap<>();
	private static Map<String, String> stationYCord = new HashMap<>();
	
    public static void main(String[] args) {
    }
    
    /**
     * Using the subway.csv file, this method populates the hashmaps with values and keys for each station code.
     */
    private static void readAndParseCsv() {
    	
    	// Get directory of subway.csv file
    	String currentDirectory = System.getProperty("user.dir");
		String filePath = currentDirectory + File.separator + "data\\subway.csv";
		
		// Read the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            // Skip the header
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                
                // Delimter
                String[] values = line.split(",");
                
                // Gets the station code, name, and coordinates
                if (values.length >= 5) {
                    String stationCode = values[3].trim();
                    String stationName = values[4].trim();
					
					String xCord = values[5].trim();
					String yCord = values[6].trim();
					
					// Add values to hashmaps
                    stationData.put(stationCode, stationName);
					stationXCord.put(stationCode, xCord);
					stationYCord.put(stationCode, yCord);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Getters
    public static String getNameByCode(String code) {
        readAndParseCsv();
        return stationData.get(code);
    }
	public static String getXCordByCode(String code) {
        readAndParseCsv();
        return stationXCord.get(code);
	}
	public static String getYCordByCode(String code) {
        readAndParseCsv();
        return stationYCord.get(code);
	}
}


