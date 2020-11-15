package com.example.demo.controller;

import com.example.demo.Util.JwtUtil;
import com.example.demo.Util.ResponseGenerator;
import com.example.demo.dto.Book;
import com.example.demo.dto.Release;
import com.example.demo.dto.ReleaseBookDto;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.service.BookService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BookController {

    @Autowired
    BookService bookService;
    Logger log= LoggerFactory.getLogger(getClass());

    @PostMapping("/release")
    public SuccessResponse releaseBook(@RequestBody ReleaseBookDto releaseBookDto, HttpServletRequest request)
    {
        String userId= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        Book book=new Book();
        book.setDetails(releaseBookDto.getDetails());
        book.setPrice(releaseBookDto.getPrice());
        book.setName(releaseBookDto.getBookName());
        book.setUrl(releaseBookDto.getUrl());
        bookService.insertBook(book);
        Release release=new Release();
        release.setUserId(userId);
        release.setDate(releaseBookDto.getDate());
        release.setBookId(book.getUid());
        bookService.insertRelease(release);
        return ResponseGenerator.getSuccessResponse();
    }

    @PostMapping("/getRelease")
    public SuccessResponse getRelease(@RequestParam("num") int number)
    {
       return ResponseGenerator.getSuccessResponse(bookService.getNewRelease(number));
    }
}
