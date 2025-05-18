package com.rentaluser.model;

import java.io.File;
import java.util.UUID;

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
        public boolean addUser(User user) {
            try {
                // Check if username already exists
                System.out.println("Adding new user: " + user.getUsername());

                if (getUserByUsername(user.getUsername()) != null) {
                    System.out.println("Username already exists: " + user.getUsername());
                    return false;
                }

                // Generate a unique ID if not provided
                if (user.getUserId() == null || user.getUserId().isEmpty()) {
                    user.setUserId(UUID.randomUUID().toString());
                }

                users.add(user);
                boolean saved = saveUsers();
                System.out.println("User saved successfully: " + saved);
                return saved;
            } catch (Exception e) {
                System.err.println("Exception occurred when adding user:");
                e.printStackTrace();
                return false;
            }
        }
        public boolean updateUser(User updatedUser) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                    users.set(i, updatedUser);
                    return saveUsers();
                }
            }
            return false;
        }


