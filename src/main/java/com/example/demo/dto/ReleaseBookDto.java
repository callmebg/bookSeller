package com.example.demo.dto;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class ReleaseBookDto {

    private String url;

    private String bookName;

    private double price;

    private String details;

    private String date;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public String getDetails() {
        return details;
    }

    public double getPrice() {
        return price;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
