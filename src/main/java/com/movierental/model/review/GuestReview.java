package com.movierental.model.review;

import java.util.Date;

public class GuestReview extends Review {
        private String emailAddress;
        private String ipAddress;
}
public GuestReview(String reviewId, String movieId, String userName, String comment, int rating, Date reviewDate, String emailAddress, String ipAddress) {
        super(reviewId, movieId, (String)null, userName, comment, rating, reviewDate);
        this.emailAddress = emailAddress;
        this.ipAddress = ipAddress;
}

public GuestReview(Review review, String emailAddress, String ipAddress) {
        super(review.getReviewId(), review.getMovieId(), (String)null, review.getUserName(), review.getComment(), review.getRating(), review.getReviewDate());
        this.emailAddress = emailAddress;
        this.ipAddress = ipAddress;
}

public GuestReview() {
        this.setUserId((String)null);
        this.emailAddress = "";
        this.ipAddress = "";
}

