package com.movierental.model.recommendation;

import java.util.Date;

public class PersonalRecommendation extends Recommendation {
    private String baseSource;      // Source of the recommendation (e.g., "watch history", "similar genre")
    private double relevanceScore;  // How relevant the recommendation is to the user (0-1)

    // Constructor with all fields
    public PersonalRecommendation(String recommendationId, String movieId, String userId,
                                  Date generatedDate, double score, String reason,
                                  String baseSource, double relevanceScore) {
        super(recommendationId, movieId, userId, generatedDate, score, reason);
        this.baseSource = baseSource;
        this.relevanceScore = relevanceScore;
    }

    // Constructor with parent fields
    public PersonalRecommendation(Recommendation recommendation, String baseSource, double relevanceScore) {
        super(recommendation.getRecommendationId(), recommendation.getMovieId(), recommendation.getUserId(),
                recommendation.getGeneratedDate(), recommendation.getScore(), recommendation.getReason());
        this.baseSource = baseSource;
        this.relevanceScore = relevanceScore;
    }

    // Default constructor
    public PersonalRecommendation() {
        super();
        this.baseSource = "";
        this.relevanceScore = 0.0;
    }

}
