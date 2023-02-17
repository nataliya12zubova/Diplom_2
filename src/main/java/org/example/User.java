package org.example;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    public String email;
    public String password;
    public String name;


    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {
    }

    public User setName(String name){
        this.name = name;
        return this;
    }

    public User setPassword(String password){
        this.password = password;
        return this;
    }

    public User setEmail(String email){
        this.email = email;
        return this;
    }

    public static User getRandom() {
        final String email = RandomStringUtils.randomAlphabetic(6) + "@gmail.com";
        final String password = RandomStringUtils.randomAlphabetic(6);
        final String name = RandomStringUtils.randomAlphabetic(6);
        return new User(email, password, name);
    }

    public static User getUserWithoutName() {
        return new User().setEmail(RandomStringUtils.randomAlphabetic(6) + "@gmail.com").setPassword(RandomStringUtils.randomAlphabetic(6));
    }

    public static User getUserWithoutPassword() {
        return new User().setEmail(RandomStringUtils.randomAlphabetic(6) + "@gmail.com").setName(RandomStringUtils.randomAlphabetic(6));
    }

    public static User getUserWithoutEmail() {
        return new User().setPassword(RandomStringUtils.randomAlphabetic(6)).setName(RandomStringUtils.randomAlphabetic(6));
    }
}
