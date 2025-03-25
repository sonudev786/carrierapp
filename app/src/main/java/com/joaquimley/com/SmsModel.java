package com.joaquimley.com;

public class SmsModel {
    private String sender;
    private String message;
    private String date;

    public SmsModel(String sender, String message, String date) {
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getDate(){
        return date;
    }
}
