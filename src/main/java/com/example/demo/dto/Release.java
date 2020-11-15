package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@TableName("release_book")
public class Release {

    private String userId;

    private String bookId;

    private String date;

    public String getBookId() {
        return bookId;
    }

    public String getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
