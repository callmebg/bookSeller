package com.example.demo.service;

import com.example.demo.dto.Book;
import com.example.demo.dto.BookDo;
import com.example.demo.exception.CustomException;

import java.util.List;

public interface BookCollectService {

    void collectBook(String userId,String bookId) throws CustomException;

    void cancelCollectBook(String userId,String bookId) throws CustomException;

    List<BookDo> selectAllCollectBook(String userId);
}
