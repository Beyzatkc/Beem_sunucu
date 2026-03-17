package com.beem.beem_sunucu.Block;

import com.beem.beem_sunucu.ApiResponse.StatusType;

public enum BlockStatus implements StatusType {
    USER_BLOCKED,
    YOU_BLOCKER,
    TARGET_BLOCKER;

    @Override
    public String getCode() {
        return this.name();
    }
}
