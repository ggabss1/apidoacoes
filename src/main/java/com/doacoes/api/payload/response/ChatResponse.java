package com.doacoes.api.payload.response;

public class ChatResponse {
    private Long id;
    private Long user_id;
    private String user_name;
    private Long unreadCount;
    private boolean hasUnreadMessages;

    public ChatResponse(Long id, Long user_id, String user_name, Long unreadCount, boolean hasUnreadMessages) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.unreadCount = unreadCount;
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }
    
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isHasUnreadMessages() {
        return hasUnreadMessages;
    }

    public void setHasUnreadMessages(boolean hasUnreadMessages) {
        this.hasUnreadMessages = hasUnreadMessages;
    }
}