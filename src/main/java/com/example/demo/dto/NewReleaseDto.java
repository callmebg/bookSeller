package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class NewReleaseDto {
    private String userId;

    private String username;

    @JsonProperty("book_id")
    private String uid;

    private String name;

    private String url;

    private double price;

    private String date;

    public double getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
