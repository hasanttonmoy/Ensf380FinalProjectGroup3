package ca.ucalgary.edu.ensf380;

import java.sql.*;

public interface IDatabaseConnection {
	void createConnection();

	ResultSet selectAds();

	void insertAds(String title, String subtitle, String text, String mediaType, String mediaPath, Date startDate,
			Date endDate, int duration);

	void deleteAds();

	void close();
}
