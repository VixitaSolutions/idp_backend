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
    private Integer cLevel;

    @Column(name = "gName")
    private String gName;

    @Column(name = "gLevel")
    private Long gLevel;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "description")
    private String description;

    @Column(name = "tenantId")
    private String tenantId;



    public CompetencyMap() {
    }

    public CompetencyMap(long id, String cName, Integer cLevel, String gName, Long gLevel, String keywords, String tenantId, String description) {
        this.cName = cName;
        this.cLevel = cLevel;
        this.gName = gName;
        this.gLevel = gLevel;
        this.keywords = keywords;
        this.tenantId = tenantId;
        this.description = description;
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

    public Integer getcLevel() {
        return cLevel;
    }

    public void setcLevel(Integer cLevel) {
        this.cLevel = cLevel;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getgLevel() {
        return gLevel;
    }

    public void setgLevel(Long gLevel) {
        this.gLevel = gLevel;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
