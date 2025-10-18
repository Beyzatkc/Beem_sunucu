package com.beem.beem_sunucu.Comments;

import jakarta.validation.constraints.NotBlank;

public class Comment_DTO_Update {
    @NotBlank(message = "Yorum içeriği boş olamaz")
    private String contents;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
