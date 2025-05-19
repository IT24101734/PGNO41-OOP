package com.movierental.model.recommendation;


import java.io.File;
import java.io.IOException;

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


}
