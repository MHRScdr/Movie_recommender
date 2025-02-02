package DataBase;

import main.TMBD;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDatabaseCreator {
    private static final String NEO4J_URI = "bolt://localhost:7687";
    private static final String NEO4J_USER = "neo4j";
    private static final String NEO4J_PASSWORD = "password";

    public static void main(String[] args) {
        Neo4jDatabase neo4j = new Neo4jDatabase(NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD);

        // ایجاد کاربران
        UserFetcher.setUsers();
        ArrayList<String> users = UserFetcher.users;

        // دریافت لیست فیلم‌ها
        List<String> movies = MovieTitlesFetcher.getMovieTitles();

        // ایجاد رابطه‌های تصادفی بین کاربران و فیلم‌ها
        Random random = new Random();
        for (String user : users) {
            int numberOfFavoriteMovies = random.nextInt(10) + 1; // هر کاربر بین 1 تا 10 فیلم را دوست دارد
            for (int i = 0; i < numberOfFavoriteMovies; i++) {
                String movie = movies.get(random.nextInt(movies.size()));
                neo4j.runQuery("MERGE (u:User {name: '" + user + "'})");
                neo4j.runQuery("MERGE (m:Movie {title: '" + movie + "'})");
                neo4j.runQuery("MATCH (u:User {name: '" + user + "'}), (m:Movie {title: '" + movie + "'}) MERGE (u)-[:LIKES]->(m)");

                // دریافت اطلاعات کامل فیلم و ذخیره در دیتابیس
                TMBD movieInfo = new TMBD(movie);
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.overview = '" + movieInfo.getMovieOverview() + "'");
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.release_date = '" + movieInfo.getMovieReleaseDate() + "'");
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.genres = '" + movieInfo.getMovieGenre() + "'");
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.runtime = " + movieInfo.getMovieRuntime());
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.popularity = " + movieInfo.getMoviePopularity());
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.vote_average = " + movieInfo.getMovieVoteAverage());
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.vote_count = " + movieInfo.getMovieVoteCount());
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.poster_path = '" + movieInfo.getMoviePosterPath() + "'");
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.directors = '" + movieInfo.getDirectorsName() + "'");
                neo4j.runQuery("MATCH (m:Movie {title: '" + movie + "'}) SET m.cast = '" + movieInfo.getCastNames() + "'");
            }
        }

        neo4j.close();
    }
}