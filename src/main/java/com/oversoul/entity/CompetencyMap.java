package com.oversoul.entity;

import javax.persistence.*;

@Entity
@Table(name = "competency_map")
public class CompetencyMap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "cName")
    private String cName;

    @Column(name = "cLevel")
    private String cLevel;

    @Column(name = "gName")
    private String gName;

    @Column(name = "gLevel")
    private String gLevel;

    @Column(name = "keywords")
    private String keywords;


    public String getTenantId(String tenantId) {
        return this.tenantId;
    }

    public CompetencyMap setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return null;
    }

    @Column(name = "tenantId")
    private String tenantId;
    public CompetencyMap() {
    }

    public CompetencyMap(long id, String cName, String cLevel, String gName, String gLevel, String keywords, String tenantId) {
        this.cName = cName;
        this.cLevel = cLevel;
        this.gName = gName;
        this.gLevel = gLevel;
        this.keywords = keywords;
        this.tenantId = tenantId;
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

    public String getcLevel() {
        return cLevel;
    }

    public void setcLevel(String cLevel) {
        this.cLevel = cLevel;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgLevel() {
        return gLevel;
    }

    public void setgLevel(String gLevel) {
        this.gLevel = gLevel;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
