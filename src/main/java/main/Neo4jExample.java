package org.example;

import org.neo4j.driver.*;

import java.util.Properties;
import java.util.Random;

public class Neo4jExample implements AutoCloseable {
    private final Driver driver;

    public Neo4jExample(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void createUserWithMovies() {
        UserFetcher.setUsers();
        MovieTitleFetcher.getMovieTitles();
        Random rand = new Random();
        for(User user: User.users){
            int numOfFavoriteMovies = rand.nextInt(10) + 1;
            for(int i = 0; i < numOfFavoriteMovies; i++){
                user.setFavoritMoviesName(MovieTitleFetcher.movieTitles.get(rand.nextInt(MovieTitleFetcher.movieTitles.size())));
            }
        }


        try (Session session = driver.session()) {
            for (User user : User.users) {
                session.writeTransaction(tx -> {
                    tx.run("CREATE (a:User {name: $name, username: $username, password: $password})",
                            org.neo4j.driver.Values.parameters("name", user.getName(), "username", user.getUsername(), "password", user.getPassword()));
                    return null;
                });

                for (TMBD movie : user.getFavoritMoviesName()) {

                    session.writeTransaction(tx -> {
                        tx.run("MERGE (m:Movie {title: $title}) " +
                                        "SET m.overview = $overview, " +
                                        "m.release_date = $releaseDate, " +
                                        "m.runtime = $runtime, " +
                                        "m.popularity = $popularity, " +
                                        "m.vote_average = $voteAverage, " +
                                        "m.vote_count = $voteCount, " +
                                        "m.poster_path = $posterPath",
                                org.neo4j.driver.Values.parameters("title", movie.getMovieTitle(),
                                        "overview", movie.getMovieOverview(),
                                        "releaseDate", movie.getMovieReleaseDate(),
                                        "runtime", movie.getMovieRuntime(),
                                        "popularity", movie.getMoviePopularity(),
                                        "voteAverage", movie.getMovieVoteAverage(),
                                        "voteCount", movie.getMovieVoteCount(),
                                        "posterPath", "https://image.tmdb.org/t/p/w500" + movie.getMoviePosterPath()));
                        return null;
                    });

                    // ایجاد رابطه بین فیلم و ژانرهای آن
                    for (String genre : movie.getMovieGenre()) {
                        session.writeTransaction(tx -> {
                            tx.run("MERGE (g:Genre {name: $genre})",
                                    org.neo4j.driver.Values.parameters("genre", genre));
                            return null;
                        });

                        session.writeTransaction(tx -> {
                            tx.run("MATCH (m:Movie {title: $title}), (g:Genre {name: $genre}) " +
                                            "MATCH (m)-[:BELONGS_TO]->(g)",
                                    org.neo4j.driver.Values.parameters("title", movie.getMovieTitle(), "genre", genre));
                            return null;
                        });
                    }

                    // ایجاد رابطه بین فیلم و کارگردان‌های آن
                    for (String director : movie.getDirectorsName()) {
                        session.writeTransaction(tx -> {
                            tx.run("MATCH (d:Director {name: $director})",
                                    org.neo4j.driver.Values.parameters("director", director));
                            return null;
                        });

                        session.writeTransaction(tx -> {
                            tx.run("MATCH (m:Movie {title: $title}), (d:Director {name: $director}) " +
                                            "MATCH (d)-[:DIRECTED]->(m)",
                                    org.neo4j.driver.Values.parameters("title", movie.getMovieTitle(), "director", director));
                            return null;
                        });
                    }

                    // ایجاد رابطه بین کاربر و فیلم
                    session.writeTransaction(tx -> {
                        tx.run("MATCH (u:User {username: $username}), (m:Movie {title: $title}) " +
                                        "MATCH (u)-[:LIKES]->(m)",
                                Values.parameters("username", user.getUsername(), "title", movie.getMovieTitle()));
                        return null;
                    });
                }
            }
        }





    }



    public static void main(String... args) throws Exception {
        Properties props = Neo4jConfig.loadProperties();
        if (props != null) {
            try (Neo4jExample example = new Neo4jExample(
                    props.getProperty("neo4j.uri"),      // اصلاح مقدار
                    props.getProperty("neo4j.username"), // اصلاح مقدار
                    props.getProperty("neo4j.password")  // اصلاح مقدار
            )) {
                example.createUserWithMovies();
            }

        }
    }
}
