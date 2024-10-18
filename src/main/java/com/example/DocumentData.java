package com.example;


public class DocumentData {
    private String id;
    private String title;
    private String author;
    private String content;

    public DocumentData(String id, String title, String author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
}
