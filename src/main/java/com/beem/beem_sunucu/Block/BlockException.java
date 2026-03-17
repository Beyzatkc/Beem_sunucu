package com.beem.beem_sunucu.Block;


public class BlockException extends RuntimeException {
    private BlockStatus status;

    public BlockException(String message) {
        super(message);
    }

    public BlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockException(String message, BlockStatus status) {
        super(message);
        this.status = status;
    }

    public BlockStatus getStatus() {
        return status;
    }
}