package com.example.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<user> {

    @Select("SELECT id FROM user WHERE username=#{username}")
    String getUserIdByName(@Param("username") String username);

    @Select("SELECT password FROM user WHERE id=#{uid}")
    String getUserPasswordById(@Param("uid") String uid);

    @Select("SELECT username FROM user WHERE username=#{username}")
    List<String> getAllUsername(@Param("username") String username);

    @Update("UPDATE user SET password=#{password} WHERE id=#{uid} ")
    void changePassword(@Param("password")String password,@Param("uid") String uid);

    @Update("UPDATE user SET permissions=#{permission} WHERE id=#{uid}")
    void changePermissions(@Param("permission") String permission,@Param("uid") String uid);

    default IPage<user> selectPage(int pageNo,int pageSize,String name)
    {
        return selectPage(new Page<>(pageNo,pageSize),new QueryWrapper<user>().eq("username",name));
    }

}
