package com.android.dogefoodie;

public class Article {
    private int id;
    private String title;
    private String author;
    private String content;
    private String category;
    private String publicationDate;

    public Article(int id, String title, String author, String content, String category, String publicationDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.category = category;
        this.publicationDate = publicationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                '}';
    }
}

