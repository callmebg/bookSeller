package com.example.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.Book;
import com.example.demo.dto.BookCollect;
import com.example.demo.dto.BookDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookCollectMapper extends BaseMapper<BookCollect> {

    default int deleteCollect(String bookId,String userId)
    {
        return delete(new QueryWrapper<BookCollect>().eq("user_id",userId).eq("book_id",bookId));
    }

    @Select("SELECT release_book.user_id,uid,name,price,url,details FROM book,user_collect,release_book WHERE user_collect.user_id=#{userId}" +
            " AND user_collect.book_id=book.uid AND book.uid=release_book.book_id")
    List<BookDo> selectAllCollect(@Param("userId") String userId);
}
