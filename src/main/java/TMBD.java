import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TMBD {
    private static final String API_KEY = "b752f10060dab915b447288ecd333f61";
    private static final String LANGUAGE = "fa_IR";
    private JsonObject movieData;
    private JsonObject creditsData;
    private final int movieId;
    private String movieTitle;
    private String movieOverview;
    private String movieReleaseDate;
    private String movieGenre;
    private int movieRuntime;
    private double moviePopularity;
    private double movieVoteAverage;
    private double movieVoteCount;
    private String moviePosterPath;
    private List<String> castNames;
    private List<String> directorsName;

    public TMBD(String movieName) {
        this.movieId = getMovieId(movieName , API_KEY , LANGUAGE);
        getMovieId(movieName , API_KEY , LANGUAGE);
        getMovieData();
        getCreditsData();
        setAll();
    }

    public void setAll(){
        this.movieTitle = getMovieTitle();
        this.movieOverview = getMovieOverview();
        this.movieReleaseDate = getMovieReleaseDate();
        this.movieGenre = getMovieGenre();
        this.movieRuntime = getMovieRuntime();
        this.moviePopularity = getMoviePopularity();
        this.movieVoteAverage = getMovieVoteAverage();
        this.movieVoteCount = getMovieVoteCount();
        this.moviePosterPath = getMoviePosterPath();
    }

    public int getMovieId(String movieName , String API_KEY , String LANGUAGE){
        try{
            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + movieName + "&language=" + LANGUAGE;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if(connection.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                response.append(line);
            }

            connection.disconnect();

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray results = jsonResponse.getAsJsonArray("results");
            if(results.size() > 0){
                JsonObject movie = results.get(0).getAsJsonObject();
                int movieId = movie.get("id").getAsInt();
                return movieId;
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void getMovieData (){
        try{
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY + "&language="+ LANGUAGE;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET"); // تغییر این خط به "GET"
            connection.setRequestProperty("Accept", "application/json");

            if(connection.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                response.append(line);
            }
            connection.disconnect();

            Gson gson = new Gson();
            movieData = gson.fromJson(response.toString(), JsonObject.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getCreditsData (){
        try {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY + "&language=" + LANGUAGE;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            if(connection.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                response.append(line);
            }
            connection.disconnect();

            Gson gson = new Gson();
            creditsData = gson.fromJson(response.toString(), JsonObject.class);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String getMovieTitle() {
        return movieData.get("title").getAsString();
    }

    public String getMovieOverview() {
        return movieData.get("overview").getAsString();
    }

    public String getMovieReleaseDate() {
        return movieData.get("release_date").getAsString();
    }

    public String getMovieGenre() {
        JsonArray genres = movieData.getAsJsonArray("genres");
        StringBuilder genreString = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            JsonObject genre = genres.get(i).getAsJsonObject();
            String genreName = genre.get("name").getAsString();
            genreString.append(genreName);
        }
        return genreString.toString();
    }

    public int getMovieRuntime() {
        return movieData.get("runtime").getAsInt();
    }

    public double getMoviePopularity() {
        return movieData.get("popularity").getAsDouble();
    }

    public double getMovieVoteAverage() {
        return movieData.get("vote_average").getAsDouble();
    }

    public double getMovieVoteCount() {
        return movieData.get("vote_count").getAsDouble();
    }

    public String getMoviePosterPath() {
        return movieData.get("poster_path").getAsString();
    }

    public List<String> getCastNames() {
        castNames = new ArrayList<>();
        JsonArray castArray = creditsData.getAsJsonArray("cast");
        for(int i = 0; i < castArray.size(); i++){
            JsonObject cast = castArray.get(i).getAsJsonObject();
            String castName = cast.get("name").getAsString();
            castNames.add(castName);
        }
        return castNames;
    }

    public List<String> getDirectorsName() {
        directorsName = new ArrayList<>();
        JsonArray directorArray = creditsData.getAsJsonArray("crew");
        for(int i = 0; i < directorArray.size(); i++){
            JsonObject crewMember = directorArray.get(i).getAsJsonObject();
            String job = crewMember.get("job").getAsString();
            if(job.equals("Director")){
                String directorName = crewMember.get("name").getAsString();
                directorsName.add(directorName);
            }
        }
        return directorsName;
    }

    public static void main(String[] args) {
        // نام فیلمی که می‌خواهید اطلاعات آن را دریافت کنید
        String movieName = "Inception";

        // ایجاد شیء از کلاس TMBD
        TMBD movie = new TMBD(movieName);

        // چاپ اطلاعات فیلم
        System.out.println("Title: " + movie.getMovieTitle());
        System.out.println("Overview: " + movie.getMovieOverview());
        System.out.println("Release Date: " + movie.getMovieReleaseDate());
        System.out.println("Genres: " + movie.getMovieGenre());
        System.out.println("Runtime: " + movie.getMovieRuntime() + " minutes");
        System.out.println("Popularity: " + movie.getMoviePopularity());
        System.out.println("Vote Average: " + movie.getMovieVoteAverage());
        System.out.println("Vote Count: " + movie.getMovieVoteCount());
        System.out.println("Poster Path: https://image.tmdb.org/t/p/w500" + movie.getMoviePosterPath());
        System.out.println("director: " + movie.getDirectorsName());
        System.out.println("casts: " + movie.getCastNames());
    }
}
