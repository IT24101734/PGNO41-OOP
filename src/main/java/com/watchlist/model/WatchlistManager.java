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


        // Initialize file paths
        private void initializeFilePaths() {
            if (servletContext != null) {
                // Use WEB-INF/data within the application context
                String webInfDataPath = "/WEB-INF/data";
                watchlistFilePath = servletContext.getRealPath(webInfDataPath) + File.separator + WATCHLIST_FILE_NAME;
                recentlyWatchedFilePath = servletContext.getRealPath(webInfDataPath) + File.separator + RECENTLY_WATCHED_FILE_NAME;

                // Ensure directories exist
                File dataDir = new File(servletContext.getRealPath(webInfDataPath));
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created WEB-INF/data directory: " + dataDir.getAbsolutePath() + " - Success: " + created);
                }
            } else {
                // Fallback to simple data directory if not in web context
                String dataPath = "data";
                watchlistFilePath = dataPath + File.separator + WATCHLIST_FILE_NAME;
                recentlyWatchedFilePath = dataPath + File.separator + RECENTLY_WATCHED_FILE_NAME;

                // Ensure directories exist
                File dataDir = new File(dataPath);
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
                }
            }

            System.out.println("WatchlistManager: Using watchlist file path: " + watchlistFilePath);
            System.out.println("WatchlistManager: Using recently watched file path: " + recentlyWatchedFilePath);
        }

        // Load watchlists from file
        private void loadWatchlists() {
            File file = new File(watchlistFilePath);
