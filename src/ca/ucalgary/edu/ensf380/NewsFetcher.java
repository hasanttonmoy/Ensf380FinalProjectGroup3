package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsFetcher {
    static class Article {
        private String title;
        private String summary;

        public Article(String title, String summary) {
            this.title = title;
            this.summary = summary;
        }
        public String getTitle() {
        	return(title);
        }
        public String getSummary() {
        	return(summary);
        }
    }
	
    private static final String DEFAULT_API_KEY = "VPksIerBsbl-LhZFwrxG3rdarmRXwM9jZ-UC6clNiK8";

    public static Article fetchNews(String query, String apiKey) {
        String apiUrl = "https://api.newscatcherapi.com/v2/search?q=" + query + "&page=1&lang=en";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-api-key", apiKey);

            int statusCode = conn.getResponseCode();
            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseData = response.toString();

                int startIndex = responseData.indexOf("[{");
                int endIndex = responseData.lastIndexOf("}]");
                String articlesData = responseData.substring(startIndex, endIndex + 2);


                String[] articles = articlesData.split("\\},\\{");

                if (articles.length > 0) {
                    String articleData = articles[0];
                    String title = extractValue(articleData, "title").replaceAll("\\s+", " ").replaceAll("\\\\n", "");
                    String summary = extractValue(articleData, "summary").replaceAll("\\\\n", "");

                    title = title + ". ";
                    Article article = new Article(title, summary);

                    return article;
                }
            } else {
                System.out.println("Request failed with status code: " + statusCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Article fetchNews(String query) {
        return fetchNews(query, DEFAULT_API_KEY);
    }
	
    private static String extractValue(String jsonData, String key) {
        int keyIndex = jsonData.indexOf("\"" + key + "\":\"");
        if (-1 == keyIndex) {
            return "";
        }
        int valueStartIndex = keyIndex + key.length() + 4; // +4 to skip ":"
        int valueEndIndex = jsonData.indexOf("\"", valueStartIndex);
        return jsonData.substring(valueStartIndex, valueEndIndex);
    }
}
