package com.watchlist.model;

public class WatchlistManager {

    public class WatchlistManager {
        private static final String WATCHLIST_FILE_NAME = "watchlists.txt";
        private static final String RECENTLY_WATCHED_FILE_NAME = "recently_watched.txt";

        private String watchlistFilePath;
        private String recentlyWatchedFilePath;
        private List<Watchlist> watchlists;
        private Map<String, RecentlyWatched> recentlyWatchedMap;
        private ServletContext servletContext;

        // Default constructor
        public WatchlistManager() {
            this(null);
        }

        // Constructor with ServletContext
        public WatchlistManager(ServletContext servletContext) {
            this.servletContext = servletContext;
            watchlists = new ArrayList<>();
            recentlyWatchedMap = new HashMap<>();
            initializeFilePaths();
            loadWatchlists();
            loadRecentlyWatched();
        }

