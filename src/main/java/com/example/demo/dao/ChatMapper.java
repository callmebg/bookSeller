package com.example.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.Chat;
import com.example.demo.dto.ChatDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatMapper extends BaseMapper<Chat> {

    @Select("SELECT * FROM chat WHERE sen_id=#{senId} AND rec_id=#{recId}")
    List<Chat> getChatMessage(@Param("senId") String senId,@Param("recId") String recId);

    default int insertChat(Chat chat)
    {
        return insert(chat);
    }

    @Select("SELECT sen_id,rec_id,details,date,a.username AS sen_name, b.username AS rec_name  FROM chat,user a,user b WHERE (sen_id=#{userId} OR rec_id=#{userId}) AND a.id=sen_id AND b.id=rec_id")
    List<ChatDo> getOwnChat(@Param("userId") String userId);
}
