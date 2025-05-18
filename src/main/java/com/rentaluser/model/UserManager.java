package com.rentaluser.model;

import java.io.File;

public class UserManager {
    public class UserManager {
        private static final String USER_FILE_NAME = "users.txt";
        private List<User> users;
        private ServletContext servletContext;
        private String dataFilePath;

        // Constructor
        public UserManager() {
            this(null);
        }

        // Constructor with ServletContext
        public UserManager(ServletContext servletContext) {
            this.servletContext = servletContext;
            users = new ArrayList<>();
            initializeFilePath();
            loadUsers();

        }

        private void initializeFilePath() {
            if (servletContext != null) {
                // Use WEB-INF/data within the application context
                String webInfDataPath = "/WEB-INF/data";
                dataFilePath = servletContext.getRealPath(webInfDataPath) + File.separator + USER_FILE_NAME;

                // Make sure directory exists
                File dataDir = new File(servletContext.getRealPath(webInfDataPath));
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created WEB-INF/data directory: " + dataDir.getAbsolutePath() + " - Success: " + created);
                }
            } else {
                // Fallback to simple data directory if not in web context
                String dataPath = "data";
                dataFilePath = dataPath + File.separator + USER_FILE_NAME;

                // Make sure directory exists
                File dataDir = new File(dataPath);
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
                }
            }

            System.out.println("UserManager: Using data file path: " + dataFilePath);
        }


