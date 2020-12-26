package com.example.bookseller;

//我的聊天记录中各个项
public class ChatListItem {
    private String uuid;
    private String name;
    public ChatListItem(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
    public String getUuid(){
        return uuid;
    }
    public String getName(){
        return name;
    }
}
