package com.beem.beem_sunucu.Posts;

import jakarta.validation.constraints.NotBlank;

public class Post_DTO_Update {
    @NotBlank(message = "Gönderi adi boş olamaz")
    private String postName;

    @NotBlank(message = "Gönderi içeriği boş olamaz")
    private String contents;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

}
