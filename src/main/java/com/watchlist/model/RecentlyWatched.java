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

