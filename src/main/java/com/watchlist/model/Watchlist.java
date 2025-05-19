package com.watchlist.model;



    public class Watchlist {
        private String watchlistId;
        private String userId;
        private String movieId;
        private Date addedDate;
        private boolean watched;
        private Date watchedDate;    // will be null if not watched
        private int priority;        // 1-5, with 1 being highest priority
        private String notes;        // optional user notes

        // Constructor with all fields
        public Watchlist(String watchlistId, String userId, String movieId, Date addedDate,
                         boolean watched, Date watchedDate, int priority, String notes) {
            this.watchlistId = watchlistId;
            this.userId = userId;
            this.movieId = movieId;
            this.addedDate = addedDate;
            this.watched = watched;
            this.watchedDate = watchedDate;
            this.priority = priority;
            this.notes = notes;
        }
