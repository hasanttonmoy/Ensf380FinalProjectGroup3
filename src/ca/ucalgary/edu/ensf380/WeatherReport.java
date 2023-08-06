package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherReport {
    private static final String DEFAULT_API_KEY = "5fdefdda2457466fb45b3023b72408d5";

    private static String fetchWeatherData(int cityCode, String apiKey) throws Exception {

        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?id=" + cityCode + "&appid=" + apiKey;


        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        conn.setRequestMethod("GET");


        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new Exception("Error: Unable to fetch data. Response Code: " + responseCode);
        }
    }

    public static List<String> getWeatherReport(int cityCode) {
        return getWeatherReport(cityCode, DEFAULT_API_KEY);
    }

    public static List<String> getWeatherReport(int cityCode, String apiKey) {
        List<String> weatherReport = new ArrayList<>();

        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = DEFAULT_API_KEY;
        }

        try {
            String response = fetchWeatherData(cityCode, apiKey);

            Pattern temperaturePattern = Pattern.compile("\"temp\":(\\d+\\.\\d+)");
            Pattern conditionsPattern = Pattern.compile("\"description\":\"([^\"]+)\"");
            Pattern cityNamePattern = Pattern.compile("\"name\":\"([^\"]+)\"");
            Pattern countryPattern = Pattern.compile("\"country\":\"([^\"]+)\"");

            Matcher temperatureMatcher = temperaturePattern.matcher(response);
            Matcher conditionsMatcher = conditionsPattern.matcher(response);
            Matcher cityNameMatcher = cityNamePattern.matcher(response);
            Matcher countryMatcher = countryPattern.matcher(response);

            if (temperatureMatcher.find() && conditionsMatcher.find() && cityNameMatcher.find() && countryMatcher.find()) {
                double temperature = Double.parseDouble(temperatureMatcher.group(1)) - 273.15;
                String conditions = conditionsMatcher.group(1);
                String cityName = cityNameMatcher.group(1);
                String country = countryMatcher.group(1);

                weatherReport.add("City: " + cityName + ", " + country);
                weatherReport.add("Temperature: " + String.format("%.2f", temperature) + " Celsius");
                weatherReport.add("Conditions: " + conditions);
            } else {
                weatherReport.add("Error: Unable to extract data from JSON response.");
            }
        } catch (Exception e) {
            weatherReport.add("Error: " + e.getMessage());
        }

        return weatherReport;
    }

    public static void main(String[] args) {
        }
    }
