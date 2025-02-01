package DataBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieTitlesFetcher {

    private static final String API_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String API_KEY = "b752f10060dab915b447288ecd333f61";
    public static List<String> movieTitles;


    public static void main(String[] args) {
        movieTitles = getMovieTitles();
        for (String title : movieTitles) {
            System.out.println(title);
        }
    }

    public static List<String> getMovieTitles() {
        List<String> movieTitles = new ArrayList<>();
        int page = 1;

        try {
            while (movieTitles.size() < 500) {
                URL url = new URL(API_URL + "?api_key=" + API_KEY + "&page=" + page);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                conn.disconnect();

                JSONObject json = new JSONObject(content.toString());
                JSONArray results = json.getJSONArray("results");

                for (int i = 0; i < results.length() && movieTitles.size() < 500 ; i++) {
                    JSONObject movie = results.getJSONObject(i);
                    movieTitles.add(movie.getString("title"));
                }

                page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieTitles;
    }

}
