package com.example.sms.models;

public class Messages {
    String address, body, date;
    int type;

    public Messages(int type, String address, String body, String date) {
        this.type = type;
        this.address = address;
        this.body = body;
        this.date = date;
    }

    public Messages() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
