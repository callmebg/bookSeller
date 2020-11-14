package com.example.demo;

import com.example.demo.dao.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookmallApplicationTests {
    @Autowired
    UserMapper userMapper;

    private Logger log= LoggerFactory.getLogger(getClass());
    @Test
    void contextLoads() {
    }

    @Test
    void test01()
    {
        log.info(userMapper.selectPage(1,1,"cf").toString());
    }

}
