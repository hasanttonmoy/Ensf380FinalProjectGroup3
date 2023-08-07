package ca.ucalgary.edu.ensf380;

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
                advertisements.add(new Advertisement(title, subtitle, text, mediaPath));
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
}
