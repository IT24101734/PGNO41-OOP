package com.rentaluser.model;

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


