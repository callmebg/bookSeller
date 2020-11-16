package com.example.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.BookCollect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BookCollectMapper extends BaseMapper<BookCollect> {

    default int deleteCollect(String bookId,String userId)
    {
        return delete(new QueryWrapper<BookCollect>().eq("user_id",userId).eq("book_id",bookId));
    }
}
