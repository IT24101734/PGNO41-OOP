package com.movierental.model.recommendation;

import java.util.Date;

public class Recommendation {

    public class Recommendation {
        private String recommendationId;
        private String movieId;
        private String userId;    // null for general recommendations
        private Date generatedDate;
        private double score;     // recommendation score/strength
        private String reason;    // reason for recommendation

        // Constructor with all fields
        public Recommendation(String recommendationId, String movieId, String userId,
                              Date generatedDate, double score, String reason) {
            this.recommendationId = recommendationId;
            this.movieId = movieId;
            this.userId = userId;
            this.generatedDate = generatedDate;
            this.score = score;
            this.reason = reason;
        }

        // Default constructor
        public Recommendation() {
            this.recommendationId = "";
            this.movieId = "";
            this.userId = null;
            this.generatedDate = new Date();
            this.score = 0.0;
            this.reason = "";
        }
}
