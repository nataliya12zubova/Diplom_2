package org.example;

import org.apache.commons.lang3.RandomStringUtils;

public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials () {
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public UserCredentials setEmail (String email){
        this.email = email;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserCredentials setPassword (String password) {
        this.password = password;
        return this;
    }

    public static UserCredentials getCredentialsWithEmailOnly (User user) {
        return new UserCredentials().setEmail(user.getEmail());
    }
    public static UserCredentials getCredentialsWithPasswordOnly (User user) {
        return new UserCredentials().setPassword(user.getPassword());
    }

    public static UserCredentials getCredentialsWithRandomEmail (User user) {
        return new UserCredentials(RandomStringUtils.randomAlphabetic(6) + "@gmail.com", user.getPassword());
    }

    public static UserCredentials getCredentialsWithRandomPassword (User user) {
        return new UserCredentials(user.getEmail(), RandomStringUtils.randomAlphabetic(6));
    }
}
