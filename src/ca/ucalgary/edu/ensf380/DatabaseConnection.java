package ca.ucalgary.edu.ensf380;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

//import java.util.ArrayList;
//import java.util.List;

/**
 * The Database class provides methods to connect to a MySQL database and
 * perform CRUD operations on the advertisements table.
 */
public class DatabaseConnection implements IDatabaseConnection {
	private Connection dbConnect;
	private ResultSet results;

	/**
	 * Default constructor.
	 */
	public DatabaseConnection() {

	}

	/**
	 * Establishes a connection to the MySQL database.
	 */
	@Override
	public void createConnection() {
		try {
			Properties props = new Properties();
			FileInputStream in = new FileInputStream(".//data//db.properties");
			props.load(in);
			in.close();

			String url = props.getProperty("url");
			String username = props.getProperty("username");
			String password = props.getProperty("password");

			dbConnect = DriverManager.getConnection(url, username, password);
			System.out.println("Connected to the database!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to the database.");
		}
	}

	/**
	 * Retrieves and prints all advertisements from the database.
	 */
	@Override
	public ResultSet selectAds() {
		ResultSet results = null;
		try {
			String query = "SELECT * FROM advertisements";
			Statement statement = dbConnect.createStatement();
			results = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to select advertisements.");
		}
		return results;
	}

//	    try {
//	        String query = "SELECT * FROM advertisements";
//	        Statement statement = dbConnect.createStatement();
//	        results = statement.executeQuery(query);
//	        while (results.next()) {
//	            System.out.println("ID: " + results.getInt("id"));
//	            System.out.println("Title: " + results.getString("title"));
//	            System.out.println("Subtitle: " + results.getString("subtitle"));
//	            System.out.println("Text: " + results.getString("text"));
//	            System.out.println("Media Type: " + results.getString("media_type"));
//	            System.out.println("Media Path: " + results.getString("media_path"));
//	            System.out.println("Start Date: " + results.getDate("start_date"));
//	            System.out.println("End Date: " + results.getDate("end_date"));
//	            System.out.println("Duration: " + results.getInt("duration"));
//	            System.out.println("-----------------------------");
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	        System.out.println("Failed to select advertisements.");
//	    }
//	    
//	    
//	}

	/**
	 * Inserts a new advertisement into the database.
	 *
	 * @param title     The title of the advertisement.
	 * @param subtitle  The subtitle of the advertisement.
	 * @param text      The text content of the advertisement.
	 * @param mediaType The media type of the advertisement.
	 * @param mediaPath The path to the media file.
	 * @param startDate The start date of the advertisement.
	 * @param endDate   The end date of the advertisement.
	 * @param duration  The duration of the advertisement.
	 */
	@Override
	public void insertAds(String title, String subtitle, String text, String mediaType, String mediaPath,
			Date startDate, Date endDate, int duration) {
		try {
			// Check for existing record with the same values
			String checkQuery = "SELECT * FROM advertisements WHERE title = ? AND subtitle = ? AND text = ? AND media_type = ? AND media_path = ? AND start_date = ? AND end_date = ? AND duration = ?";
			PreparedStatement checkStatement = dbConnect.prepareStatement(checkQuery);
			checkStatement.setString(1, title);
			checkStatement.setString(2, subtitle);
			checkStatement.setString(3, text);
			checkStatement.setString(4, mediaType);
			checkStatement.setString(5, mediaPath);
			checkStatement.setDate(6, startDate);
			checkStatement.setDate(7, endDate);
			checkStatement.setInt(8, duration);
			ResultSet checkResults = checkStatement.executeQuery();
			if (checkResults.next()) {
				System.out.println("Advertisement with the same values already exists. Skipping insertion.");
				return; // Exit the method if a matching record is found
			}

			// Insert new record
			String query = "INSERT INTO advertisements (title, subtitle, text, media_type, media_path, start_date, end_date, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = dbConnect.prepareStatement(query);
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, subtitle);
			preparedStatement.setString(3, text);
			preparedStatement.setString(4, mediaType);
			preparedStatement.setString(5, mediaPath);
			preparedStatement.setDate(6, startDate);
			preparedStatement.setDate(7, endDate);
			preparedStatement.setInt(8, duration);
			preparedStatement.executeUpdate();
			System.out.println("Advertisement inserted successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to insert advertisement.");
		}
	}

	@Override
	public void deleteAds() {
		// not needed then delete
	}

	/**
	 * Closes the ResultSet and Connection objects.
	 */
	@Override
	public void close() {
		try {
			if (results != null) { // Check if results is not null before closing
				results.close();
			}
			if (dbConnect != null) { // check dbConnect before closing
				dbConnect.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * Main method to demonstrate the usage of the Database class.
//	 *
//	 * @param args Command-line arguments (not used).
//	 */
//	public static void main(String[] args) {
//		DatabaseConnection dbConnection = new DatabaseConnection();
//		dbConnection.createConnection();
//
//		// Inserting sample advertisements
//		dbConnection.insertAds("Food Promotion", "Mexican Food", "Location: 123 Road NW", "JPEG",
//		        ".//media//mexfood.jpg", Date.valueOf("2023-08-01"), Date.valueOf("2023-08-31"), 10);
//		dbConnection.insertAds("Colgate", "Toothpaste", "STRENGTHENS WEAKEND ENAMELS", "JPEG", 
//		        ".//media//colgate.jpeg", Date.valueOf("2023-08-01"), Date.valueOf("2023-09-30"), 10);
//		dbConnection.insertAds("Merchant Warehouse", "Agent & ISO PROGRAM", "Join the program today. Call 800-743-8047"
//		        + " to learn more", "JPEG", ".//media//hire.jpg", Date.valueOf("2023-08-01"), Date.valueOf("2023-09-30"), 10);
//		dbConnection.insertAds("Careers", "We are Hiring", "Social Media Manager Digital Marketing Specialist", 
//		        "JPEG", ".//media//agent.jpg", Date.valueOf("2023-08-01"), Date.valueOf("2023-09-1"), 10);
//
//		AdvertisementManager manager = new AdvertisementManager();
//		manager.loadAdvertisementsFromDatabase(dbConnection);
//
//		// Create and display the GUI
//		new AdvertisementDisplay(manager.getAdvertisements());
//
//		// Get the advertisements and print their media paths
//		List<Advertisement> ads = manager.getAdvertisements(); // Assuming you have a getter for the advertisements list
//		for (Advertisement ad : ads) {
//		    System.out.println(ad.getMediaPath());
//		}
//		dbConnection.close();
//
//	}
}
