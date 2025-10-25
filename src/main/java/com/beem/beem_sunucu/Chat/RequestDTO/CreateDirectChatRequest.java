package com.beem.beem_sunucu.Chat.RequestDTO;


public class CreateDirectChatRequest {

    private String title;

    private String description;

    private Long targetId;

    private Long myId;

    public CreateDirectChatRequest(){}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getMyId() {
        return myId;
    }

    public void setMyId(Long myId) {
        this.myId = myId;
    }
}
