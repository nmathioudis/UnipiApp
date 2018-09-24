package com.example.nikos.unipiapp;

public class FavoriteNewsInformation {
    private String content, source, title, urlImage;

    public FavoriteNewsInformation() {
    }

    public FavoriteNewsInformation(String title) {
        this.title = title;
    }

    public FavoriteNewsInformation(String content, String source, String title, String urlImage) {
        this.content = content;
        this.source = source;
        this.title = title;
        this.urlImage = urlImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
