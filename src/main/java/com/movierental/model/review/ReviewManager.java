package com.movierental.model.review;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReviewManager {
    private static final String REVIEW_FILE_NAME = "reviews.txt";
    private List<Review> reviews;
    private ServletContext servletContext;
    private String dataFilePath;

    public ReviewManager() {
        this((ServletContext) null);
    }

    public ReviewManager(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.reviews = new ArrayList();
        this.initializeFilePath();
        this.loadReviews();
    }

    private void initializeFilePath() {
        if (this.servletContext != null) {
            String webInfDataPath = "/WEB-INF/data";
            String var10001 = this.servletContext.getRealPath(webInfDataPath);
            this.dataFilePath = var10001 + File.separator + "reviews.txt";
            File dataDir = new File(this.servletContext.getRealPath(webInfDataPath));
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                PrintStream var10000 = System.out;
                var10001 = dataDir.getAbsolutePath();
                var10000.println("Created WEB-INF/data directory: " + var10001 + " - Success: " + created);
            }
        } else {
            String dataPath = "data";
            this.dataFilePath = dataPath + File.separator + "reviews.txt";
            File dataDir = new File(dataPath);
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
            }
        }

        System.out.println("ReviewManager: Using data file path: " + this.dataFilePath);
    }

    private void loadReviews() {
        File file = new File(this.dataFilePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            boolean created = file.getParentFile().mkdirs();
            PrintStream var10000 = System.out;
            String var10001 = file.getParentFile().getAbsolutePath();
            var10000.println("Created directory for reviews: " + var10001 + " - Success: " + created);
        }

        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                System.out.println("Created reviews file: " + this.dataFilePath + " - Success: " + created);
            } catch (IOException e) {
                System.err.println("Error creating reviews file: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        Review review = null;
                        if (line.startsWith("VERIFIED_REVIEW,")) {
                            review = VerifiedReview.fromFileString(line);
                        } else if (line.startsWith("GUEST_REVIEW,")) {
                            review = GuestReview.fromFileString(line);
                        } else if (line.startsWith("REVIEW,")) {
                            review = Review.fromFileString(line);
                        }

                        if (review != null) {
                            this.reviews.add(review);
                            System.out.println("Loaded review: " + review.getReviewId());
                        }
                    }
                }

                System.out.println("Total reviews loaded: " + this.reviews.size());
            } catch (IOException e) {
                System.err.println("Error loading reviews: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void saveReviews() {
        try {
            File file = new File(this.dataFilePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                boolean created = file.getParentFile().mkdirs();
                PrintStream var10000 = System.out;
                String var10001 = file.getParentFile().getAbsolutePath();
                var10000.println("Created directory for reviews: " + var10001 + " - Success: " + created);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.dataFilePath))) {
                for (Review review : this.reviews) {
                    writer.write(review.toFileString());
                    writer.newLine();
                }
            }

            System.out.println("Reviews saved successfully to: " + this.dataFilePath);
        } catch (IOException e) {
            System.err.println("Error saving reviews: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public Review addRegularReview(String userId, String userName, String movieId, String comment, int rating) {
        for (Review existingReview : this.reviews) {
            if (existingReview.getMovieId().equals(movieId) && userId.equals(existingReview.getUserId())) {
                return null;
            }
        }

        Review review = new Review();
        review.setReviewId(UUID.randomUUID().toString());
        review.setUserId(userId);
        review.setUserName(userName);
        review.setMovieId(movieId);
        review.setComment(comment);
        review.setRating(this.validateRating(rating));
        review.setReviewDate(new Date());
        this.reviews.add(review);
        this.saveReviews();
        this.updateMovieRating(movieId);
        return review;
    }

    public VerifiedReview addVerifiedReview(String userId, String userName, String movieId, String transactionId, String comment, int rating) {
        for (Review existingReview : this.reviews) {
            if (existingReview.getMovieId().equals(movieId) && userId.equals(existingReview.getUserId())) {
                return null;
            }
        }

        RentalManager rentalManager = new RentalManager(this.servletContext);
        Transaction transaction = rentalManager.getTransactionById(transactionId);
        if (transaction != null && transaction.isReturned() && transaction.getUserId().equals(userId) && transaction.getMovieId().equals(movieId)) {
            VerifiedReview review = new VerifiedReview();
            review.setReviewId(UUID.randomUUID().toString());
            review.setUserId(userId);
            review.setUserName(userName);
            review.setMovieId(movieId);
            review.setComment(comment);
            review.setRating(this.validateRating(rating));
            review.setReviewDate(new Date());
            review.setTransactionId(transactionId);
            review.setWatchDate(transaction.getReturnDate());
            this.reviews.add(review);
            this.saveReviews();
            this.updateMovieRating(movieId);
            return review;
        } else {
            return null;
        }
    }

    public GuestReview addGuestReview(String guestName, String movieId, String comment, int rating, String emailAddress, String ipAddress) {
        GuestReview review = new GuestReview();
        review.setReviewId(UUID.randomUUID().toString());
        review.setUserName(guestName);
        review.setMovieId(movieId);
        review.setComment(comment);
        review.setRating(this.validateRating(rating));
        review.setReviewDate(new Date());
        review.setEmailAddress(emailAddress);
        review.setIpAddress(ipAddress);
        this.reviews.add(review);
        this.saveReviews();
        this.updateMovieRating(movieId);
        return review;
    }
}
