package com.example.lmsnew;

public class Feedback {
    private String course;
    private float rating;
    private String recommend;
    private String lectureId;
    private String comments;

    public Feedback() {
        // Default constructor required for calls to DataSnapshot.getValue(Feedback.class)
    }

    public Feedback(String course, float rating, String recommend, String lectureId, String comments) {
        this.course = course;
        this.rating = rating;
        this.recommend = recommend;
        this.lectureId = lectureId;
        this.comments = comments;
    }

    // Getters and setters (if needed)
}
