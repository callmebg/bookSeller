package com.example.bookseller;

public class PutOnEvent {
    String uuid;
    String url;
    String price;
    String name;
    String detail;

    public PutOnEvent(){

    }

    public PutOnEvent(String uuid, String url, String price, String name, String detail){
        this.uuid = uuid;
        this.url = url;
        this.price = price;
        this.name = name;
        this.detail = detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
