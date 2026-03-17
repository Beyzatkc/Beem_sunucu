package com.beem.beem_sunucu.ApiResponse;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private StatusType status;
    private LocalDateTime timestamp;

    public ApiResponse() {}

    public ApiResponse(boolean success, T data, String message, StatusType status) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> error(String message, StatusType status){
        return new ApiResponse<>(false, null, message, status);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
