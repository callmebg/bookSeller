package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ChatDo {
    private String senId;

    private String recId;

    private String details;

    private String date;

    private String recName;

    private String senName;


    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRecId() {
        return recId;
    }

    public String getSenId() {
        return senId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public void setSenId(String senId) {
        this.senId = senId;
    }

    public String getRecName() {
        return recName;
    }

    public String getSenName() {
        return senName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public void setSenName(String senName) {
        this.senName = senName;
    }
}
