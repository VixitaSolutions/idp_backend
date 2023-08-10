package com.oversoul.entity;

import javax.persistence.*;

@Entity
@Table(name = "youtube_link")
public class youtubeLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "cName")
    private String cName;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    public youtubeLinks() {
    }

    public youtubeLinks(long id, String cName, String name, String description, String url) {
        this.id = id;
        this.cName = cName;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
