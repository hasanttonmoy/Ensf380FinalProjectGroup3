package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class TrainArray {
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
        public String toString() {
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
	

	public static Train[] parseCsvFile(String filePath) {
		List<Train> trains = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
					continue;
				}
				String[] data = line.split(",");
				if (data.length == 5) {
					String lineName = data[0].trim();
					int trainNumber = Integer.parseInt(data[1].trim());
					String stationCode = data[2].trim();
					String direction = data[3].trim();
					String destination = data[4].trim();
					
					String input = stationCode;
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
					
					String nextStation = input.replaceAll(regex, String.format("%02d", nextCode));
					String prevStation = input.replaceAll(regex, String.format("%02d", prevCode));

					Train train = new Train(lineName, trainNumber, stationCode, direction, destination, nextStation, prevStation);
					trains.add(train);
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

		return trains.toArray(new Train[0]);
	}

    public static void main(String[] args) {
    }
}
