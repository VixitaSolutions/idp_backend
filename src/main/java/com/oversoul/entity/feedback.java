package com.oversoul.entity;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
public class feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "rating")
    private String rating;

    public feedback() {
    }

    public feedback(long id, String feedback, String rating) {
        this.id = id;
        this.feedback = feedback;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
