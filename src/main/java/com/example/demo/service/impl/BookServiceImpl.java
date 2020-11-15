package com.example.demo.service.impl;

import com.example.demo.dao.BookMapper;
import com.example.demo.dao.ReleaseMapper;
import com.example.demo.dto.Book;
import com.example.demo.dto.NewReleaseDto;
import com.example.demo.dto.Release;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;
    @Autowired
    ReleaseMapper releaseMapper;
    @Override
    public void insertBook(Book book) {
        bookMapper.insert(book);
    }

    @Override
    public void insertRelease(Release release) {
        releaseMapper.insert(release);
    }

    @Override
    public List<NewReleaseDto> getNewRelease(int number) {
        return releaseMapper.getNewRelease(number);
    }
}
