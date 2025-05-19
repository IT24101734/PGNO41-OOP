package com.movierental.model.review;

import java.io.*;
import java.util.*;

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


public boolean updateReview(String reviewId, String userId, String comment, int rating) {
    for(int i = 0; i < this.reviews.size(); ++i) {
        Review review = (Review)this.reviews.get(i);
        if (review.getReviewId().equals(reviewId)) {
            if (userId != null && !userId.equals(review.getUserId())) {
                return false;
            }

            review.setComment(comment);
            review.setRating(this.validateRating(rating));
            this.saveReviews();
            this.updateMovieRating(review.getMovieId());
            return true;
        }
    }

    return false;
}

public boolean deleteReview(String reviewId, String userId) {
    for(int i = 0; i < this.reviews.size(); ++i) {
        Review review = (Review)this.reviews.get(i);
        if (review.getReviewId().equals(reviewId)) {
            if (userId != null && !userId.equals(review.getUserId())) {
                return false;
            }

            String movieId = review.getMovieId();
            this.reviews.remove(i);
            this.saveReviews();
            this.updateMovieRating(movieId);
            return true;
        }
    }

    return false;
}

public Review getReviewById(String reviewId) {
    for(Review review : this.reviews) {
        if (review.getReviewId().equals(reviewId)) {
            return review;
        }
    }

    return null;
}

public List<Review> getReviewsByMovie(String movieId) {
    List<Review> movieReviews = new ArrayList();

    for(Review review : this.reviews) {
        if (review.getMovieId().equals(movieId)) {
            movieReviews.add(review);
        }
    }

    Collections.sort(movieReviews, new Comparator<Review>() {
        public int compare(Review r1, Review r2) {
            return r2.getReviewDate().compareTo(r1.getReviewDate());
        }
    });
    return movieReviews;
}

public List<Review> getReviewsByUser(String userId) {
    List<Review> userReviews = new ArrayList();

    for(Review review : this.reviews) {
        if (userId.equals(review.getUserId())) {
            userReviews.add(review);
        }
    }

    Collections.sort(userReviews, new Comparator<Review>() {
        public int compare(Review r1, Review r2) {
            return r2.getReviewDate().compareTo(r1.getReviewDate());
        }
    });
    return userReviews;
}

public boolean hasUserReviewedMovie(String userId, String movieId) {
    for(Review review : this.reviews) {
        if (review.getMovieId().equals(movieId) && userId.equals(review.getUserId())) {
            return true;
        }
    }

    return false;
}

public Review getUserReviewForMovie(String userId, String movieId) {
    for(Review review : this.reviews) {
        if (review.getMovieId().equals(movieId) && userId.equals(review.getUserId())) {
            return review;
        }
    }

    return null;
}

public double calculateAverageRating(String movieId) {
    List<Review> movieReviews = this.getReviewsByMovie(movieId);
    if (movieReviews.isEmpty()) {
        return (double)0.0F;
    } else {
        int totalRating = 0;

        for(Review review : movieReviews) {
            totalRating += review.getRating();
        }

        return (double)totalRating / (double)movieReviews.size();
    }
}

public int countVerifiedReviews(String movieId) {
    int count = 0;

    for(Review review : this.reviews) {
        if (review.getMovieId().equals(movieId) && review.isVerified()) {
            ++count;
        }
    }

    return count;
}

public int countGuestReviews(String movieId) {
    int count = 0;

    for(Review review : this.reviews) {
        if (review.getMovieId().equals(movieId) && !review.isVerified()) {
            ++count;
        }
    }

    return count;
}

public Map<Integer, Integer> getRatingDistribution(String movieId) {
    Map<Integer, Integer> distribution = new HashMap();

    for(int i = 1; i <= 5; ++i) {
        distribution.put(i, 0);
    }

    for(Review review : this.reviews) {
        if (review.getMovieId().equals(movieId)) {
            int rating = review.getRating();
            distribution.put(rating, (Integer)distribution.get(rating) + 1);
        }
    }

    return distribution;
}

public List<Review> getAllReviews() {
    return new ArrayList(this.reviews);
}

public List<Review> getRecentReviews(int count) {
    List<Review> allReviews = new ArrayList(this.reviews);
    Collections.sort(allReviews, new Comparator<Review>() {
        public int compare(Review r1, Review r2) {
            return r2.getReviewDate().compareTo(r1.getReviewDate());
        }
    });
    int limit = Math.min(count, allReviews.size());
    return allReviews.subList(0, limit);
}

private void updateMovieRating(String movieId) {
    double averageRating = this.calculateAverageRating(movieId);
    averageRating = (double)Math.round(averageRating * (double)10.0F) / (double)10.0F;
    MovieManager movieManager = new MovieManager(this.servletContext);
    Movie movie = movieManager.getMovieById(movieId);
    if (movie != null) {
        movie.setRating(averageRating);
        movieManager.updateMovie(movie);
    }

}

private int validateRating(int rating) {
    if (rating < 1) {
        return 1;
    } else {
        return rating > 5 ? 5 : rating;
    }
}

public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
    this.initializeFilePath();
    this.reviews.clear();
    this.loadReviews();
}
}

