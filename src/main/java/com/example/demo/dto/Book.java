package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("book")
@Builder
public class Book {
    @TableId("uid")
    private String uid;

    private String name;

    private double price;

    private String url;

    private String details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getUid() {
        return uid;
    }

    public String getUrl() {
        return url;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
