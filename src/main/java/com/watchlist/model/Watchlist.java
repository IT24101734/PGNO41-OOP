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

