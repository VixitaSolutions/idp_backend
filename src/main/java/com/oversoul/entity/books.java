package com.oversoul.entity;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class books {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "cName")
    private String cName;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publishingYear")
    private String publishingYear;

    @Column(name = "Title")
    private String Title;

    @Column(name = "url")
    private String url;

    public books() {
    }

    public books(String cName, String author, String publisher, String publishingYear, String title, String url) {

        this.cName = cName;
        this.author = author;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        Title = title;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishingYear() {
        return publishingYear;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
