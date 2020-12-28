package com.example.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.Book;
import com.example.demo.dto.ReleaseBookDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookMapper extends BaseMapper<Book> {

    @Select("SELECT uid,name,price,url,details,date FROM book,release_book WHERE user_id=#{userId} AND" +
            " book_id=uid")
    List<ReleaseBookDo> getMyRelease(@Param("userId") String userId);

    default int deleteRelease(String bookId)
    {
        return delete(new QueryWrapper<Book>().eq("uid",bookId));
    }

    default Book selectBookById(String bookId)
    {
        return selectOne(new QueryWrapper<Book>().eq("uid",bookId));
    }

    @Update("UPDATE book SET url=#{url} WHERE uid=#{bookId}")
    void updateUrlById(@Param("bookId") String bookId,@Param("url")String url);

    default List<Book> selectBookByName(String bookName)
    {
        return selectList(new QueryWrapper<Book>().eq("name",bookName));
    }

}
