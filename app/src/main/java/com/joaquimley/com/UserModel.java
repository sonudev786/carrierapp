package com.joaquimley.com;

public class UserModel {
    private String username;
    private String name;
    private String password;

    public UserModel(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword(){
        return password;
    }
}
