package com.example.demo.service;

import com.example.demo.dto.Book;
import com.example.demo.dto.NewReleaseDto;
import com.example.demo.dto.Release;
import com.example.demo.dto.ReleaseBookDo;
import com.example.demo.exception.CustomException;

import java.util.List;

public interface BookService {

    void insertBook(Book book);

    void insertRelease(Release release);

    List<NewReleaseDto>  getNewRelease(int number);

    List<ReleaseBookDo> getMyRelease(String userId);

    void cancelRelease(String userId,String bookId) throws CustomException;

    List<Book> getBookByName(String bookName);
}
