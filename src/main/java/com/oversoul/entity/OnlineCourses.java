package com.oversoul.entity;

import javax.persistence.*;

@Entity
@Table(name = "onlineCourses")
public class OnlineCourses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "cName")
    private String cName;

    @Column(name = "course")
    private String course;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "platform")
    private String platform;

    @Column(name = "imgUrl")
    private String imgUrl;
    public OnlineCourses(){

    }
    public OnlineCourses(long id, String cName, String course, String description, String url, String platform, String imgUrl) {
        this.id = id;
        this.cName = cName;
        this.course = course;
        this.description = description;
        this.url = url;
        this.platform = platform;
        this.imgUrl = imgUrl;
    }

    public long getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public String getCourse() {
        return course;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPlatform() {
        return platform;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
