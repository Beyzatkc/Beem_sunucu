package com.beem.beem_sunucu.Users;

public class CustomExceptions {
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
    public  static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
    public static class  NotFoundException extends RuntimeException {
        public  NotFoundException(String message) {
            super(message);
        }
    }
    public static class  AuthorizationException extends RuntimeException {
        public AuthorizationException(String message) {
            super(message);
        }
    }
}
