package com.ya;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

import java.util.Locale;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    public static Faker faker = new Faker((new Locale("en")));


    public String email;
    public String password;
    public String name;


    public User(){

    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    @JsonInclude
    public static User getRandomUser(){
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(6,10);
        final String name = faker.pokemon().name();
        return new User(email,password,name);
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public static User getEmail(User user) {
        return new User().setEmail(user.email);
    }

    public static User getWithoutPassword(){
        return new User().setEmail(faker.internet().emailAddress()).setName(faker.pokemon().name());
    }

    public static User getWithoutEmail(){
        return new User().setPassword(faker.internet().password(6,10)).setName(faker.pokemon().name());
    }

    public static User getWithoutName(){
        return new User().setPassword(faker.internet().password()).setEmail(faker.internet().emailAddress());
    }

    public static User getWithEmailOnly(){
        return new User().setEmail(faker.internet().emailAddress());
    }



    public static User getWithNameOnly(){
        return new User().setName(faker.pokemon().name());
    }

    public static User getWithPasswordOnly(){
        return new User().setPassword(faker.internet().password());
    }

    public static User getEmpty(){
        return new User().setEmail(null).setName(null).setPassword(null);
    }










}
