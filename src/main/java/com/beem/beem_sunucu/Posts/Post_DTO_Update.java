package com.beem.beem_sunucu.Posts;

import jakarta.validation.constraints.NotBlank;

public class Post_DTO_Update {
    @NotBlank(message = "Gönderi adi boş olamaz")
    private String postName;

    @NotBlank(message = "Gönderi içeriği boş olamaz")
    private String contents;

    @NotBlank(message = "Gönderi kategorisi boş olamaz")
    private String categories;

    private String coverImage;

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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
