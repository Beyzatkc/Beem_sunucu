package com.beem.beem_sunucu.Verification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {
    @NotBlank(message = "Parola boş olamaz")
    @Size(min = 6, message = "Parola en az 6 karakter olmalı")
    private String newPassword;

    @NotBlank(message = "Parola boş olamaz")
    @Size(min = 6, message = "Parola en az 6 karakter olmalı")
    private String confirmPassword;

    public ResetPasswordDTO(){

    }
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
