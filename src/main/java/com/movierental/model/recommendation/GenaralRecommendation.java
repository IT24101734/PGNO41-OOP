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
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean isPersonalized() {
        return false;
    }
    @Override
    public String toFileString() {
        return "GENERAL_RECOMMENDATION," + super.getRecommendationId() + "," + super.getMovieId() + "," +
                "null," + super.getGeneratedDate().getTime() + "," +
                super.getScore() + "," + super.getReason().replace(",", "\\,") + "," +
                category + "," + rank;
    }
}
