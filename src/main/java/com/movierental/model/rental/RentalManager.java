package com.movierental.model.rental;

import java.io.*;
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

        // Load transactions from file
        private void loadTransactions() {
            File file = new File(dataFilePath);

            // Create directory if it doesn't exist
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("Created rentals file: " + dataFilePath);
                } catch (IOException e) {
                    System.err.println("Error creating rentals file: " + e.getMessage());
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

                    Transaction transaction = Transaction.fromFileString(line);
                    if (transaction != null) {
                        transactions.add(transaction);
                        System.out.println("Loaded transaction: " + transaction.getTransactionId());
                    }
                }
                System.out.println("Total transactions loaded: " + transactions.size());
            } catch (IOException e) {
                System.err.println("Error loading transactions: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Save transactions to file
        private void saveTransactions() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
                for (Transaction transaction : transactions) {
                    writer.write(transaction.toFileString());
                    writer.newLine();
                }
                System.out.println("Transactions saved successfully to: " + dataFilePath);
            } catch (IOException e) {
                System.err.println("Error saving transactions: " + e.getMessage());
                e.printStackTrace();
            }
        }


    }
