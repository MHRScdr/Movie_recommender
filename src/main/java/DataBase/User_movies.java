package DataBase;

import main.User;

import java.util.Random;

public class User_movies {

    public void setUserMovie(){
        Random rand = new Random();
        UserFetcher.setUsers();
        MovieTitlesFetcher.getMovieTitles();


        for(int i = 0; i < 250; i ++){
            User user = new User(UserFetcher.users.get(i) , UserFetcher.users.get(i) , "****");
            int numOfFavoritMovies = rand.nextInt(10);
            for(int j = 0; j < numOfFavoritMovies; j ++){
                user.setFavoritMoviesName(MovieTitlesFetcher.movieTitles.get(rand.nextInt(MovieTitlesFetcher.movieTitles.size())));
            }
        }
    }
}
