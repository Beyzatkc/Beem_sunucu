package com.beem.beem_sunucu.Follow;

import java.util.List;

public class TestUsersDTO {

    private Long targetId;
    private List<Long> userIds;

    public TestUsersDTO() {
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
