package com.movierental.model.rental;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RentalManager {
    public class RentalManager {
        private static final String RENTAL_FILE_NAME = "rentals.txt";
        private List<Transaction> transactions;
        private MovieManager movieManager;
        private UserManager userManager;
        private ServletContext servletContext;
        private String dataFilePath;

        // Default constructor
        public RentalManager() {
            this(null);
        }

        // Constructor with existing managers
        public RentalManager(MovieManager movieManager, UserManager userManager, ServletContext servletContext) {
            transactions = new ArrayList<>();
            this.movieManager = movieManager;
            this.userManager = userManager;
            this.servletContext = servletContext;
            initializeFilePath();
            loadTransactions();
        }

        private void initializeFilePath() {
            if (servletContext != null) {
                // Use WEB-INF/data within the application context
                String webInfDataPath = "/WEB-INF/data";
                dataFilePath = servletContext.getRealPath(webInfDataPath) + File.separator + RENTAL_FILE_NAME;

                // Make sure directory exists
                File dataDir = new File(servletContext.getRealPath(webInfDataPath));
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created WEB-INF/data directory: " + dataDir.getAbsolutePath() + " - Success: " + created);
                }
            } else {
                // Fallback to simple data directory if not in web context
                String dataPath = "data";
                dataFilePath = dataPath + File.separator + RENTAL_FILE_NAME;

                // Make sure directory exists
                File dataDir = new File(dataPath);
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
                }
            }

            System.out.println("RentalManager: Using data file path: " + dataFilePath);
        }

    }
