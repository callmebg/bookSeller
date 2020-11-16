package com.example.demo.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.NewReleaseDto;
import com.example.demo.dto.Release;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReleaseMapper extends BaseMapper<Release> {

    @Select("SELECT user_id,username,uid,name,price,url,date FROM user,release_book,book WHERE user.id=release_book.user_id AND" +
            " release_book.book_id=book.uid ORDER BY date desc LIMIT 0,#{number}")
    List<NewReleaseDto> getNewRelease(@Param("number") int number);

    default int deleteRelease(String userId,String bookId)
    {
        return delete(new QueryWrapper<Release>().eq("user_id",userId).eq("book_id",bookId));
    }
}
