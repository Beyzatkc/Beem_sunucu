package com.beem.beem_sunucu.ApiResponse;

public enum ApiStatus implements StatusType {
    SUCCESS,
    ERROR,
    NOT_FOUND,
    SERVER_ERROR;

    @Override
    public String getCode() {
        return this.name();
    }
}
