package com.example.demo.service;

import com.example.demo.dto.Book;
import com.example.demo.dto.NewReleaseDto;
import com.example.demo.dto.Release;

import java.util.List;

public interface BookService {

    void insertBook(Book book);

    void insertRelease(Release release);

    List<NewReleaseDto>  getNewRelease(int number);
}
