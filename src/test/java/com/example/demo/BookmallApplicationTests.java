package com.example.demo;

import com.example.demo.dao.BookMapper;
import com.example.demo.dao.ReleaseMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.dto.Book;
import com.example.demo.dto.NewReleaseDto;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BookmallApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookService bookService;
    @Autowired
    ReleaseMapper releaseMapper;

    private Logger log= LoggerFactory.getLogger(getClass());
    @Test
    void contextLoads() {
    }

    @Test
    void test01()
    {
        log.info(userMapper.selectPage(1,1,"cf").toString());
    }

    @Test
    void test02()
    {
        Book book=new Book();
        book.setName("数学分析");
        book.setPrice(100.21);
        book.setDetails("数学分析的说明");
        bookService.insertBook(book);
    }

    @Test
    void test03()
    {
        List<NewReleaseDto> list=releaseMapper.getNewRelease(10);
        for(NewReleaseDto n:list)
        {
            log.info(n.toString());
        }
    }
}
