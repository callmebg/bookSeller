package com.example.demo.service.impl;

import com.example.demo.dao.BookCollectMapper;
import com.example.demo.dao.BookMapper;
import com.example.demo.dto.Book;
import com.example.demo.dto.BookCollect;
import com.example.demo.dto.BookDo;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionEnum;
import com.example.demo.service.BookCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCollectServiceImpl implements BookCollectService {

    @Autowired
    BookCollectMapper bookCollectMapper;
    @Autowired
    BookMapper bookMapper;
    @Override
    public void collectBook(String userId, String bookId) throws CustomException {
        if(bookMapper.selectBookById(bookId)==null) throw  new CustomException(ExceptionEnum.VALID_BOOKID,"collectBook");
        BookCollect bookCollect=new BookCollect();
        bookCollect.setBookId(bookId);
        bookCollect.setUserId(userId);
        bookCollectMapper.insert(bookCollect);
    }

    @Override
    public void cancelCollectBook(String userId, String bookId) throws CustomException {
        int Exception=bookCollectMapper.deleteCollect(bookId,userId);
        if(Exception==0) throw  new CustomException(ExceptionEnum.VALID_BOOKID,"collectBook");
    }

    @Override
    public List<BookDo> selectAllCollectBook(String userId) {
        return bookCollectMapper.selectAllCollect(userId);
    }
}
