package com.movierental.model.rental;

import java.io.*;
import java.util.*;

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

    // Rent a movie
    public Transaction rentMovie(String userId, String movieId, int rentalDays) {
        System.out.println("RentalManager: Starting rental process...");
        System.out.println("RentalManager: User ID: " + userId);
        System.out.println("RentalManager: Movie ID: " + movieId);
        System.out.println("RentalManager: Rental Days: " + rentalDays);

        // Check if user exists
        User user = userManager.getUserById(userId);
        if (user == null) {
            System.out.println("RentalManager: User not found");
            return null;
        }
        System.out.println("RentalManager: User found: " + user.getUsername());

        // Check if movie exists and is available
        Movie movie = movieManager.getMovieById(movieId);
        if (movie == null) {
            System.out.println("RentalManager: Movie not found");
            return null;
        }
        System.out.println("RentalManager: Movie found: " + movie.getTitle());

        if (!movie.isAvailable()) {
            System.out.println("RentalManager: Movie is not available");
            return null;
        }
        System.out.println("RentalManager: Movie is available");

        // Check if user has reached rental limit
        List<Transaction> userActiveRentals = getActiveRentalsByUser(userId);
        System.out.println("RentalManager: User has " + userActiveRentals.size() + " active rentals");
        System.out.println("RentalManager: User's rental limit is " + user.getRentalLimit());

        if (userActiveRentals.size() >= user.getRentalLimit()) {
            System.out.println("RentalManager: User has reached rental limit");
            return null;
        }

        // Calculate rental fee based on user type and movie type
        double rentalFee = calculateRentalFee(user, movie, rentalDays);
        System.out.println("RentalManager: Calculated rental fee: " + rentalFee);

        // Calculate due date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, rentalDays);
        Date dueDate = calendar.getTime();

        // Create new transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setUserId(userId);
        transaction.setMovieId(movieId);
        transaction.setRentalDate(new Date());
        transaction.setDueDate(dueDate);
        transaction.setRentalFee(rentalFee);
        transaction.setReturned(false);
        transaction.setCanceled(false);

        System.out.println("RentalManager: Created transaction with ID: " + transaction.getTransactionId());

        // Update movie availability
        movie.setAvailable(false);
        boolean movieUpdated = movieManager.updateMovie(movie);
        System.out.println("RentalManager: Updated movie availability: " + movieUpdated);

        // Add transaction to list and save
        transactions.add(transaction);
        saveTransactions();
        System.out.println("RentalManager: Rental transaction completed successfully");

        return transaction;
    }

    // Return a movie
    public boolean returnMovie(String transactionId) {
        System.out.println("RentalManager: Starting return process for transaction: " + transactionId);

        Transaction transaction = getTransactionById(transactionId);
        if (transaction == null) {
            System.out.println("RentalManager: Transaction not found");
            return false;
        }

        if (transaction.isReturned() || transaction.isCanceled()) {
            System.out.println("RentalManager: Movie already returned or rental canceled");
            return false;
        }

        // Set return date and update returned status
        Date returnDate = new Date();
        transaction.setReturnDate(returnDate);
        transaction.setReturned(true);

        // Calculate late fee if applicable
        if (returnDate.after(transaction.getDueDate())) {
            int daysLate = transaction.calculateDaysOverdue();
            User user = userManager.getUserById(transaction.getUserId());

            double lateFee = calculateLateFee(user, daysLate);
            transaction.setLateFee(lateFee);
            System.out.println("RentalManager: Added late fee of " + lateFee);
        }

        // Update movie availability
        Movie movie = movieManager.getMovieById(transaction.getMovieId());
        if (movie != null) {
            movie.setAvailable(true);
            movieManager.updateMovie(movie);
            System.out.println("RentalManager: Updated movie availability");
        } else {
            System.out.println("RentalManager: Warning - Movie not found when returning");
        }

        // Save updated transaction
        saveTransactions();
        System.out.println("RentalManager: Return process completed successfully");

        return true;
    }

    // Cancel a rental
    public boolean cancelRental(String transactionId, String reason) {
        System.out.println("RentalManager: Starting cancellation process for transaction: " + transactionId);

        Transaction transaction = getTransactionById(transactionId);
        if (transaction == null) {
            System.out.println("RentalManager: Transaction not found");
            return false;
        }

        if (transaction.isReturned() || transaction.isCanceled()) {
            System.out.println("RentalManager: Movie already returned or rental canceled");
            return false;
        }

        // Update transaction status
        transaction.setCanceled(true);
        transaction.setCancellationDate(new Date());
        transaction.setCancellationReason(reason);

        // Update movie availability
        Movie movie = movieManager.getMovieById(transaction.getMovieId());
        if (movie != null) {
            movie.setAvailable(true);
            movieManager.updateMovie(movie);
            System.out.println("RentalManager: Updated movie availability after cancellation");
        } else {
            System.out.println("RentalManager: Warning - Movie not found when canceling rental");
        }

        // Save updated transaction
        saveTransactions();
        System.out.println("RentalManager: Cancellation process completed successfully");

        return true;
    }


}
