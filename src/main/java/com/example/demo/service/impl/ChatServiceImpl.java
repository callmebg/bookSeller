package com.example.demo.service.impl;

import com.example.demo.dao.ChatMapper;
import com.example.demo.dto.Chat;
import com.example.demo.dto.ChatDo;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ChatMapper chatMapper;

    @Override
    public List<Chat> getChatMessage(String senId, String recId) {
        List<Chat> list=new ArrayList<>();
        list.addAll(chatMapper.getChatMessage(senId, recId));
        list.addAll(chatMapper.getChatMessage(recId,senId));
        list.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return list;
    }

    @Override
    public int insertChatList(List<Chat> chatList) {
        for(Chat chat:chatList)
        {
            chatMapper.insertChat(chat);
        }
        return 1;
    }

    @Override
    public List<ChatDo> getOwnChat(String userId) {
        List<ChatDo> list=new ArrayList<>();
        list.addAll(chatMapper.getOwnChat(userId));
        list.sort(new Comparator<ChatDo>() {
            @Override
            public int compare(ChatDo o1, ChatDo o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return list;
    }
}
