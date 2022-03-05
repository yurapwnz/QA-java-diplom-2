package com.ya;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.ya.User.faker;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentials {

    public  String email;
    public  String password;

    public UserCredentials(){

    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public static UserCredentials from(User user){
        return new UserCredentials(user.email,user.password);
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public static UserCredentials getUserAuthorizationWithEmailOnly(User user) {
        return new UserCredentials().setEmail(user.email);
    }
    public static UserCredentials getUserAuthorizationWithPasswordOnly (User user) {
        return new UserCredentials().setPassword(user.password);
    }

    public static UserCredentials getUserAuthorizationWithOnlyValidEmail (User user) {
        return new UserCredentials().setEmail(user.email).setPassword(faker.internet().password());
    }
    public static UserCredentials getUserAuthorizationWithOnlyValidPassword (User user) {
        return new UserCredentials().setEmail(faker.internet().emailAddress()).setPassword(user.password);
    }
    public static UserCredentials getUserAuthorizationWithEmpty (User user) {
        return new UserCredentials().setEmail(null).setPassword(null);
    }








}
