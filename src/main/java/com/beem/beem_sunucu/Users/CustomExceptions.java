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
    public static class ServiceException extends RuntimeException {
        public  ServiceException(String message) {
            super(message);
        }
    }
    /** Email verification token zaten kullanılmış veya duplicate */
    public static class DuplicateTokenException extends RuntimeException {
        public DuplicateTokenException(String message) { super(message); }
    }

    /** Token geçersiz veya süresi dolmuş */
    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) { super(message); }
    }

    /** Veritabanı constraint ihlali (unique, not null vb) */
    public static class DataIntegrityException extends RuntimeException {
        public DataIntegrityException(String message) { super(message); }
    }

    /** Email gönderme hatası */
    public static class EmailSendException extends RuntimeException {
        public EmailSendException(String message) { super(message); }
    }

    /** Genel validation hatası (örneğin password pattern vs) */
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) { super(message); }
    }
}
