package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * The NewsFetcher class is used to get news articles using NewsCatcherAPI and a user query.
 * The user can either use a default API key or use their own when calling the method.
 * 
 * @author Owen Guldberg
 * @version 2023-08-06
 */
public class NewsFetcher {
	/**
	 * This class stores the title and summary of a news article
	 */
    static class Article {
        private String title;
        private String summary;
        
        /**
         * Creates a new Article object from the title and summary arguments
         * 
         * @param title
         * @param summary
         */
        public Article(String title, String summary) {
            this.title = title;
            this.summary = summary;
        }
        
        // Getters
        public String getTitle() {
        	return(title);
        }
        public String getSummary() {
        	return(summary);
        }
    }
	
    // Default API key
    private static final String DEFAULT_API_KEY = "GfYGggGCiK2WmNC-5HqQs9lSml0uveo5izmCL0ATtW8";
    
    /**
     * Fetches news articles from the API based on the query using an API key
     * 
     * @param query
     * @param apiKey
     * @return An article object that stores the title and summary of the article
     */
    public static Article fetchNews(String query, String apiKey) {
    	// Create the string used to connect to the API
        String apiUrl = "https://api.newscatcherapi.com/v2/search?q=" + query + "&page=1&lang=en"; // Only recieve 1 page

        try {
        	// Create the URL
            URL url = new URL(apiUrl);
            
            // Connect to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Request to GET from the API
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-api-key", apiKey);
            
            // Check that the connection status is good
            int statusCode = conn.getResponseCode();
            
            if (statusCode == 200) {
            	
            	// Stream
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                
                String inputLine;
                StringBuffer response = new StringBuffer();
                
                // Append stream input to StringBuffer
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                
                // Close connection
                in.close();
                
                // Turn StringBuffer into a String now that no more data is being read
                String responseData = response.toString();
                
                // Only keep the articles portion of the string
                int startIndex = responseData.indexOf("[{");
                int endIndex = responseData.lastIndexOf("}]");
                String articlesData = responseData.substring(startIndex, endIndex + 2);
                
                // Delimter to split the articles
                String[] articles = articlesData.split("\\},\\{");
                
                // Save an article into an article class
                if (articles.length > 0) {
                	int randomNum = (int) (Math.random() * articles.length);
                    String articleData = articles[randomNum]; // Random article
                    
                    // Extract the title string and summary string
                    String title = extractValue(articleData, "title").replaceAll("\\s+", " ").replaceAll("\\\\n", ""); // Get rid of extra whitespace
                    String summary = extractValue(articleData, "summary").replaceAll("\\\\n", "");
                    title = title + ". ";
                    
                    // Create article object to store summary and string
                    Article article = new Article(title, summary);
                    return article;
                }
                
            } else {
                System.out.println("Request failed with status code: " + statusCode); // If code = 401 API key maybe expired
            }
            
            // Close connection
            conn.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Uses default API key if one is not provided
     * 
     * @param query
     * @return An article object that stores the title and summary of the article
     */
    public static Article fetchNews(String query) {
        return fetchNews(query, DEFAULT_API_KEY);
    }
	
    /**
     * Extracts a string from another string based on a start and end index
     * 
     * @param jsonData
     * @param key
     * @return String
     */
    private static String extractValue(String jsonData, String key) {
    	// Finds the index of a key eg. '"title":"'
        int keyIndex = jsonData.indexOf("\"" + key + "\":\"");
        if (-1 == keyIndex) {
            return "";
        }
        int valueStartIndex = keyIndex + key.length() + 4; // +4 to skip ":"
        // Find end of the string that will be saved
        int valueEndIndex = jsonData.indexOf("\"", valueStartIndex);
        
        // Returns the substring
        return jsonData.substring(valueStartIndex, valueEndIndex);
    }
}
