package com.example.demo.service;

import com.example.demo.dto.Chat;
import com.example.demo.dto.ChatDo;

import java.util.List;

public interface ChatService {

    List<Chat> getChatMessage(String senId,String recId);

    int insertChatList(List<Chat> chatList);

    List<ChatDo> getOwnChat(String userId);
}
