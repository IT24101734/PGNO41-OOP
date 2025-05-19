package com.watchlist.model;

import java.util.Date;
import java.util.Stack;

public class RecentlyWatched {
    private Stack<String> movieIds;     // Stack for movie IDs
    private Stack<Date> watchDates;     // Parallel stack for watch dates
    private int maxSize;                // maximum movies to keep track of

    // Constructor
    public RecentlyWatched(String userId) {
        this.movieIds = new Stack<>();
        this.watchDates = new Stack<>();
        this.maxSize = 10;  // default to track last 10 movies
    }

    // Constructor with customizable size
    public RecentlyWatched(String userId, int maxSize) {
        this.movieIds = new Stack<>();
        this.watchDates = new Stack<>();
        this.maxSize = maxSize;
    }
}

    public void addMovie(String movieId) {
    // Remove existing entry if it exists
        int existingIndex = movieIds.indexOf(movieId);
        if (existingIndex != -1) {
        // Remove the existing movie and its date
        movieIds.removeElementAt(existingIndex);
        watchDates.removeElementAt(existingIndex);
    }

    // Push new movie to the top of the stack
        movieIds.push(movieId);
        watchDates.push(new Date());


