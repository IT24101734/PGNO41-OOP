package com.movierental.model.review;

import java.util.Date;

    public class VerifiedReview extends Review {
        private String transactionId;
        private Date watchDate;
}

    public VerifiedReview(String reviewId, String movieId, String userId, String userName, String comment, int rating, Date reviewDate, String transactionId, Date watchDate) {
        super(reviewId, movieId, userId, userName, comment, rating, reviewDate);
        this.transactionId = transactionId;
        this.watchDate = watchDate;
    }

    public VerifiedReview(Review review, String transactionId, Date watchDate) {
        super(review.getReviewId(), review.getMovieId(), review.getUserId(), review.getUserName(), review.getComment(), review.getRating(), review.getReviewDate());
        this.transactionId = transactionId;
        this.watchDate = watchDate;
    }

    public VerifiedReview() {
        this.transactionId = "";
        this.watchDate = new Date();
    }


