package com.example.demo.service;

import com.example.demo.exception.CustomException;

public interface BookCollectService {

    void collectBook(String userId,String bookId) throws CustomException;

    void cancelCollectBook(String userId,String bookId) throws CustomException;
}
