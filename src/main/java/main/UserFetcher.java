package org.example;

import com.github.javafaker.Faker;

import java.util.ArrayList;

public class UserFetcher {
    public static ArrayList<String> users = new ArrayList<String>(250);

    public static void setUsers(){
        Faker faker = new Faker();
        for(int i = 0; i < users.size(); i++){
            users.add(faker.name().fullName());
            User user = new User(faker.name().toString() , faker.name().toString() , "****");
            User.users.add(user);
        }
    }

}