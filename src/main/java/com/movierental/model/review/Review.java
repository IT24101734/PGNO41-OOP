package com.movierental.model.review;

import java.util.Date;

public class Review {
    private String reviewId;
    private String movieId;
    private String userId;
    private String userName;
    private String comment;
    private int rating;
    private Date reviewDate;

}

public class Review {
    private String reviewId;
    private String movieId;
    private String userId;
    private String userName;
    private String comment;
    private int rating;
    private Date reviewDate;

    public Review(String reviewId, String movieId, String userId, String userName, String comment, int rating, Date reviewDate) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    public Review() {
        this.reviewId = "";
        this.movieId = "";
        this.userId = null;
        this.userName = "";
        this.comment = "";
        this.rating = 0;
        this.reviewDate = new Date();
    }

