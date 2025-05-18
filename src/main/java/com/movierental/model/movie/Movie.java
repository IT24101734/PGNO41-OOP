package com.movierental.model.movie;

public class Movie {

    // New getter and setter for coverPhotoPath
    public String getCoverPhotoPath() {
        return coverPhotoPath;
    }

    public void setCoverPhotoPath(String coverPhotoPath) {
        this.coverPhotoPath = coverPhotoPath;
    }

    // Calculate rental price (to be overridden by subclasses)
    public double calculateRentalPrice(int daysRented) {
        return 3.99 * daysRented; // Base rental price
    }

    // Update toFileString to include cover photo path
    public String toFileString() {
        return "REGULAR," + movieId + "," + title + "," + director + "," +
                genre + "," + releaseYear + "," + rating + "," + available + "," + coverPhotoPath;

    }
}