package com.example.hinduja_health_first.models;

import java.io.Serializable;

public class HealthBlog implements Serializable {
    private String title;
    private String description;
    private String imageUrl;
    private String source;
    private String publishedAt;
    private String url;

    public HealthBlog(String title, String description, String imageUrl, String source, String publishedAt, String url) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.source = source;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getSource() { return source; }
    public String getPublishedAt() { return publishedAt; }
    public String getUrl() { return url; }
} 