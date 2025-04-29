package com.nostratech.spring.model;
public class Book {
    private String id;
    private String title;
    private Author author;

    public Book(String id, String title, Author author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorId(Author author) {
        this.author = author;
    }
}
