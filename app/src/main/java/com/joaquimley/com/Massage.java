package com.joaquimley.com;

public class Massage {
    private String username;
    private String title;
    private String description;
    private String date;

    public Massage(String username, String title, String description, String date) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.date = date;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
