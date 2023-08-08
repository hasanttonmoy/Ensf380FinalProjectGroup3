package ca.ucalgary.edu.ensf380;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class AdvertisementManager {
	private List<Advertisement> advertisements;

	public AdvertisementManager() {
		advertisements = new ArrayList<>();
	}

	public void loadAdvertisementsFromDatabase(DatabaseConnection dbConnection) {
		ResultSet results = dbConnection.selectAds();
		try {
			while (results.next()) {
				String title = results.getString("title");
				String subtitle = results.getString("subtitle");
				String text = results.getString("text");
				String mediaPath = results.getString("media_path");
				Date startDate = results.getDate("start_date");
				Date endDate = results.getDate("end_date");
				advertisements.add(new Advertisement(title, subtitle, text, mediaPath, startDate, endDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printAdvertisements() {
		for (Advertisement ad : advertisements) {
			System.out.println(ad);
			System.out.println("-----------------------------");
		}
	}

	public List<Advertisement> getAdvertisements() {
		return advertisements;
	}
}
