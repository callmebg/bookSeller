package com.example.demo.controller;


import com.example.demo.Util.JwtUtil;
import com.example.demo.Util.ResponseGenerator;
import com.example.demo.dto.ChatDto;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping("/getChatMessage")
    public SuccessResponse getChatMessage(HttpServletRequest request, @RequestParam("anotherUserId")String anotherUserId)
    {
        String uid= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        return ResponseGenerator.getSuccessResponse(chatService.getChatMessage(uid,anotherUserId));
    }

    @PostMapping("/insertChatMessage")
    public SuccessResponse insertChatMessage(@RequestBody ChatDto chatDto)
    {
        chatService.insertChatList(chatDto.getChatList());
        return ResponseGenerator.getSuccessResponse();
    }

    @GetMapping("/getUserAllChatMessage")
    public SuccessResponse getUserAllChatMessage(HttpServletRequest request)
    {
        String uid= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        return ResponseGenerator.getSuccessResponse(chatService.getOwnChat(uid));
    }

}
