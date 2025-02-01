package main;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private String username;
    private List<String> favoritMoviesName = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        favoritMoviesName.add(name);
    }

    public List<String> getFavoritMoviesName(){
        return favoritMoviesName;
    }

    public User(String name , String username , String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }

}
