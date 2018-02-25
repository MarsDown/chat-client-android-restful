package com.drsaina.mars.testnotification.Data.remot.Model;


/**
 * Created by mars on 2/10/2018.
 */
public class CoreChatRoom {
    private int chatId;
    private String chatName;
    private String chatDescription;
    private String chatType;
    private String chatImageProfile;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatDescription() {
        return chatDescription;
    }

    public void setChatDescription(String chatDescription) {
        this.chatDescription = chatDescription;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getChatImageProfile ( ) {
        return chatImageProfile;
    }

    public void setChatImageProfile ( String chatImageProfile ) {
        this.chatImageProfile = chatImageProfile;
    }
}
