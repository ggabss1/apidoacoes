package com.doacoes.api.payload.response;

public class ChatMessageResponse {
    private Long id;
    private Long sender_id;
    private String text;
    private String sentAt;
    private boolean isRead;

    public ChatMessageResponse(Long id, Long sender_id, String text, String sentAt, boolean isRead) {
        this.id = id;
        this.sender_id = sender_id;
        this.text = text;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender_id() {
        return sender_id;
    }
    
    public void setSender_id(Long sender_id) {
        this.sender_id = sender_id;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}