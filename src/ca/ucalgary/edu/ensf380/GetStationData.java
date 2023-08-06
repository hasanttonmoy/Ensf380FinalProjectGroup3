package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class GetStationData {
    private static Map<String, String> stationData = new HashMap<>();
	
	private static Map<String, String> stationXCord = new HashMap<>();
	private static Map<String, String> stationYCord = new HashMap<>();
	
    public static void main(String[] args) {
    }

    private static void readAndParseCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] values = line.split(",");

                if (values.length >= 5) {
                    String stationCode = values[3].trim();
                    String stationName = values[4].trim();
					
					String xCord = values[5].trim();
					String yCord = values[6].trim();

                    stationData.put(stationCode, stationName);
					stationXCord.put(stationCode, xCord);
					stationYCord.put(stationCode, yCord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNameByCode(String code) {
		String currentDirectory = System.getProperty("user.dir");
		String inputPath = currentDirectory + File.separator + "data\\subway.csv";
        readAndParseCsv(inputPath);
        return stationData.get(code);
    }
	
	public static String getXCordByCode(String code) {
		String currentDirectory = System.getProperty("user.dir");
		String inputPath = currentDirectory + File.separator + "data\\subway.csv";
        readAndParseCsv(inputPath);
        return stationXCord.get(code);
	}
	
	public static String getYCordByCode(String code) {
		String currentDirectory = System.getProperty("user.dir");
		String inputPath = currentDirectory + File.separator + "data\\subway.csv";
        readAndParseCsv(inputPath);
        return stationYCord.get(code);
	}
}


