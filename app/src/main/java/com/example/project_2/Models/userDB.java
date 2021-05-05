package com.example.project_2.Models;

public class userDB {
    String username;
    String email;
    String password;
    String accountType;
    String profile_image;

    public userDB() {
    }

    public userDB(String username, String email, String password, String accountType,String profile_image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.profile_image =profile_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
