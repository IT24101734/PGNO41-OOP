package com.movierental.model.recommendation;

import java.util.Date;
    public class GeneralRecommendation extends Recommendation {
        private String category;      // Category of recommendation (e.g., "top-rated", "trending")
        private int rank;             // Ranking within the category

        // Constructor with all fields
        public GeneralRecommendation(String recommendationId, String movieId,
                                     Date generatedDate, double score, String reason,
                                     String category, int rank) {
            super(recommendationId, movieId, null, generatedDate, score, reason);
            this.category = category;
            this.rank = rank;
        }

        // Constructor with parent fields
        public GeneralRecommendation(com.movierental.model.recommendation.Recommendation recommendation, String category, int rank) {
            super(recommendation.getRecommendationId(), recommendation.getMovieId(), null,
                    recommendation.getGeneratedDate(), recommendation.getScore(), recommendation.getReason());
            this.category = category;
            this.rank = rank;
        }

        // Default constructor
        public GeneralRecommendation() {
            super();
            this.category = "";
            this.rank = 0;
        }

    }
