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
    public String getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getMovieId() {
        return this.movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public boolean isVerified() {
        return false;
    }

    public String toString() {
        String var10000 = this.reviewId;
        return "Review{reviewId='" + var10000 + "', movieId='" + this.movieId + "', userId='" + this.userId + "', userName='" + this.userName + "', comment='" + this.comment + "', rating=" + this.rating + ", reviewDate=" + String.valueOf(this.reviewDate) + "}";
    }
}

