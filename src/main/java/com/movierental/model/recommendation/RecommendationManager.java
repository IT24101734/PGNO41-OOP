package com.movierental.model.recommendation;


import java.io.*;
import java.util.*;

public class RecommendationManager {
    private static final String RECOMMENDATION_FILE_NAME = "recommendations.txt";
    private List<Recommendation> recommendations;
    private MovieManager movieManager;
    private ReviewManager reviewManager;
    private RentalManager rentalManager;
    private WatchlistManager watchlistManager;
    private ServletContext servletContext;
    private String dataFilePath;

    // Constructor
    public RecommendationManager() {
        this(null);
    }

    // Constructor with ServletContext
    public RecommendationManager(ServletContext servletContext) {
        this.servletContext = servletContext;
        recommendations = new ArrayList<>();
        initializeFilePath();
        loadRecommendations();

        // Initialize other managers with the same ServletContext
        movieManager = new MovieManager(servletContext);
        reviewManager = new ReviewManager(servletContext);
        rentalManager = new RentalManager(servletContext);
        watchlistManager = new WatchlistManager(servletContext);
    }
    private void initializeFilePath() {
        if (servletContext != null) {
            // Use WEB-INF/data within the application context
            String webInfDataPath = "/WEB-INF/data";
            dataFilePath = servletContext.getRealPath(webInfDataPath) + File.separator + RECOMMENDATION_FILE_NAME;

            // Make sure directory exists
            File dataDir = new File(servletContext.getRealPath(webInfDataPath));
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created WEB-INF/data directory: " + dataDir.getAbsolutePath() + " - Success: " + created);
            }
        } else {
            // Fallback to simple data directory if not in web context
            String dataPath = "data";
            dataFilePath = dataPath + File.separator + RECOMMENDATION_FILE_NAME;

            // Make sure directory exists
            File dataDir = new File(dataPath);
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
            }
        }

        System.out.println("RecommendationManager: Using data file path: " + dataFilePath);

        // Ensure the file exists
        try {
            File file = new File(dataFilePath);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created new recommendations file: " + dataFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error creating recommendations file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void loadRecommendations() {
        File file = new File(dataFilePath);

        if (!file.exists()) {
            try {
                // Create parent directories if they don't exist
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                System.out.println("Created new recommendations file: " + dataFilePath);
            } catch (IOException e) {
                System.err.println("Error creating recommendations file: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                Recommendation recommendation = null;
                if (line.startsWith("PERSONAL_RECOMMENDATION,")) {
                    recommendation = PersonalRecommendation.fromFileString(line);
                } else if (line.startsWith("GENERAL_RECOMMENDATION,")) {
                    recommendation = GeneralRecommendation.fromFileString(line);
                } else if (line.startsWith("RECOMMENDATION,")) {
                    recommendation = Recommendation.fromFileString(line);
                }

                if (recommendation != null) {
                    recommendations.add(recommendation);
                }
            }
            System.out.println("Loaded " + recommendations.size() + " recommendations from " + dataFilePath);
        } catch (IOException e) {
            System.err.println("Error loading recommendations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void saveRecommendations() {
        try {
            // Ensure parent directory exists
            File file = new File(dataFilePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
                for (Recommendation recommendation : recommendations) {
                    writer.write(recommendation.toFileString());
                    writer.newLine();
                }
            }
            System.out.println("Saved " + recommendations.size() + " recommendations to " + dataFilePath);
        } catch (IOException e) {
            System.err.println("Error saving recommendations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<GeneralRecommendation> generateGeneralRecommendations() {
        // Clear existing general recommendations
        recommendations.removeIf(rec -> !rec.isPersonalized());

        List<GeneralRecommendation> generalRecommendations = new ArrayList<>();

        // Generate top-rated recommendations
        List<Movie> topRatedMovies = movieManager.getTopRatedMovies(20); // Get top 20 rated movies

        int rank = 1;
        for (Movie movie : topRatedMovies) {
            GeneralRecommendation recommendation = new GeneralRecommendation();
            recommendation.setRecommendationId(UUID.randomUUID().toString());
            recommendation.setMovieId(movie.getMovieId());
            recommendation.setGeneratedDate(new Date());
            recommendation.setScore(movie.getRating()); // Use movie rating as score
            recommendation.setReason("This movie is one of our top-rated films with a rating of " + movie.getRating());
            recommendation.setCategory("top-rated");
            recommendation.setRank(rank++);

            generalRecommendations.add(recommendation);
            recommendations.add(recommendation);
        }

        // Get all unique genres
        Set<String> genres = new HashSet<>();
        for (Movie movie : movieManager.getAllMovies()) {
            genres.add(movie.getGenre());
        }

        // Generate recommendations by genre
        for (String genre : genres) {
            List<Movie> genreMovies = new ArrayList<>();
            for (Movie movie : movieManager.getAllMovies()) {
                if (movie.getGenre().equals(genre)) {
                    genreMovies.add(movie);
                }
            }

            // Sort by rating using bubble sort algorithm (for educational purposes)
            for (int i = 0; i < genreMovies.size() - 1; i++) {
                for (int j = 0; j < genreMovies.size() - i - 1; j++) {
                    if (genreMovies.get(j).getRating() < genreMovies.get(j + 1).getRating()) {
                        // Swap movies
                        Movie temp = genreMovies.get(j);
                        genreMovies.set(j, genreMovies.get(j + 1));
                        genreMovies.set(j + 1, temp);
                    }
                }
            }

            // Take top 5 movies per genre
            int genreRank = 1;
            for (int i = 0; i < Math.min(5, genreMovies.size()); i++) {
                Movie movie = genreMovies.get(i);

                GeneralRecommendation recommendation = new GeneralRecommendation();
                recommendation.setRecommendationId(UUID.randomUUID().toString());
                recommendation.setMovieId(movie.getMovieId());
                recommendation.setGeneratedDate(new Date());
                recommendation.setScore(movie.getRating());
                recommendation.setReason("This is one of the top-rated " + genre + " movies");
                recommendation.setCategory("genre-" + genre.toLowerCase());
                recommendation.setRank(genreRank++);

                generalRecommendations.add(recommendation);
                recommendations.add(recommendation);
            }
        }

        saveRecommendations();
        return generalRecommendations;
    }




}
