package org.example;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private String username;
    public static ArrayList<User> users = new ArrayList<User>();
    private List<TMBD> favoritMoviesName = new ArrayList<>();
    private List<String> favoritMoviesUsername = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFavoritMoviesUsername() {
        return favoritMoviesUsername;
    }

    public void setFavoritMoviesUsername(List<String> favoritMoviesUsername) {
        this.favoritMoviesUsername = favoritMoviesUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavoritMoviesName(String name){
        favoritMoviesName.add(new TMBD(name));
    }

    public List<TMBD> getFavoritMoviesName(){
        return favoritMoviesName;
    }

    public User(String name , String username , String password){
        this.name = name;
        this.username = username;
        this.password = password;
        users.add(this);
    }

}
