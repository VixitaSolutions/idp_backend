package com.oversoul.entity;

import javax.persistence.*;

@Entity
@Table(name = "preloaded_data")
public class PreloadedData {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "cName")
    private String cName;

    @Column(name = "bAuthor")
    private String bAuthor;

    @Column(name = "bPublisher")
    private String bPublisher;

    @Column(name = "bPublishingYear")
    private Integer bPublishingYear;

    @Column(name = "bTitle")
    private String bTitle;

    @Column(name = "bUrl")
    private String bUrl;

    @Column(name = "ocCourse")
    private String ocCourse;

    @Column(name = "ocDescription")
    private String ocDescription;

    @Column(name = "ocUrl")
    private String ocUrl;

    @Column(name = "ocPlatform")
    private String ocPlatform;

    @Column(name = "ocImgUrl")
    private String ocImgUrl;

    @Column(name = "yName")
    private String yName;

    @Column(name = "yDescription")
    private String yDescription;

    @Column(name = "yUrl")
    private String yUrl;

    public PreloadedData() {
    }

    public PreloadedData(long id, String cName, String bAuthor, String bPublisher, Integer bPublishingYear, String bTitle, String bUrl, String ocCourse, String ocDescription, String ocUrl, String ocPlatform, String ocImgUrl, String yName, String yDescription, String yUrl) {
        this.id = id;
        this.cName = cName;
        this.bAuthor = bAuthor;
        this.bPublisher = bPublisher;
        this.bPublishingYear = bPublishingYear;
        this.bTitle = bTitle;
        this.bUrl = bUrl;
        this.ocCourse = ocCourse;
        this.ocDescription = ocDescription;
        this.ocUrl = ocUrl;
        this.ocPlatform = ocPlatform;
        this.ocImgUrl = ocImgUrl;
        this.yName = yName;
        this.yDescription = yDescription;
        this.yUrl = yUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getbAuthor() {
        return bAuthor;
    }

    public void setbAuthor(String bAuthor) {
        this.bAuthor = bAuthor;
    }

    public String getbPublisher() {
        return bPublisher;
    }

    public void setbPublisher(String bPublisher) {
        this.bPublisher = bPublisher;
    }

    public Integer getbPublishingYear() {
        return bPublishingYear;
    }

    public void setbPublishingYear(Integer bPublishingYear) {
        this.bPublishingYear = bPublishingYear;
    }

    public String getbTitle() {
        return bTitle;
    }

    public void setbTitle(String bTitle) {
        this.bTitle = bTitle;
    }

    public String getbUrl() {
        return bUrl;
    }

    public void setbUrl(String bUrl) {
        this.bUrl = bUrl;
    }

    public String getOcCourse() {
        return ocCourse;
    }

    public void setOcCourse(String ocCourse) {
        this.ocCourse = ocCourse;
    }

    public String getOcDescription() {
        return ocDescription;
    }

    public void setOcDescription(String ocDescription) {
        this.ocDescription = ocDescription;
    }

    public String getOcUrl() {
        return ocUrl;
    }

    public void setOcUrl(String ocUrl) {
        this.ocUrl = ocUrl;
    }

    public String getOcPlatform() {
        return ocPlatform;
    }

    public void setOcPlatform(String ocPlatform) {
        this.ocPlatform = ocPlatform;
    }

    public String getOcImgUrl() {
        return ocImgUrl;
    }

    public void setOcImgUrl(String ocImgUrl) {
        this.ocImgUrl = ocImgUrl;
    }

    public String getyName() {
        return yName;
    }

    public void setyName(String yName) {
        this.yName = yName;
    }

    public String getyDescription() {
        return yDescription;
    }

    public void setyDescription(String yDescription) {
        this.yDescription = yDescription;
    }

    public String getyUrl() {
        return yUrl;
    }

    public void setyUrl(String yUrl) {
        this.yUrl = yUrl;
    }
}
