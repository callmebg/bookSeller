package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.dao.BookMapper;
import com.example.demo.dao.ReleaseMapper;
import com.example.demo.dto.Book;
import com.example.demo.dto.NewReleaseDto;
import com.example.demo.dto.Release;
import com.example.demo.dto.ReleaseBookDo;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionEnum;
import com.example.demo.service.BookService;
import com.google.common.base.Equivalence;
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

    @Override
    public List<ReleaseBookDo> getMyRelease(String userId) {
        return bookMapper.getMyRelease(userId);
    }

    @Override
    public void cancelRelease(String userId, String bookId) throws CustomException {
        int delete1=bookMapper.deleteRelease(bookId);
        int delete2=releaseMapper.deleteRelease(userId,bookId);
        if(delete2==0||delete1==0)
        {
            throw new CustomException(ExceptionEnum.DELETE_FAILED,"cancelRelease");
        }
    }


}
