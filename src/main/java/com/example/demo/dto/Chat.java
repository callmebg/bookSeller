package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TableName("chat")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Chat{
    private String senId;

    private String recId;

    private String details;

    private String date;

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

}
