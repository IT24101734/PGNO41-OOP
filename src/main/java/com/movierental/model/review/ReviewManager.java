package com.movierental.model.review;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

    public class ReviewManager {
        private static final String REVIEW_FILE_NAME = "reviews.txt";
        private List<Review> reviews;
        private ServletContext servletContext;
        private String dataFilePath;

        public ReviewManager() {
            this((ServletContext)null);
        }

        public ReviewManager(ServletContext servletContext) {
            this.servletContext = servletContext;
            this.reviews = new ArrayList();
            this.initializeFilePath();
            this.loadReviews();
        }
        private void initializeFilePath() {
            if (this.servletContext != null) {
                String webInfDataPath = "/WEB-INF/data";
                String var10001 = this.servletContext.getRealPath(webInfDataPath);
                this.dataFilePath = var10001 + File.separator + "reviews.txt";
                File dataDir = new File(this.servletContext.getRealPath(webInfDataPath));
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    PrintStream var10000 = System.out;
                    var10001 = dataDir.getAbsolutePath();
                    var10000.println("Created WEB-INF/data directory: " + var10001 + " - Success: " + created);
                }
            } else {
                String dataPath = "data";
                this.dataFilePath = dataPath + File.separator + "reviews.txt";
                File dataDir = new File(dataPath);
                if (!dataDir.exists()) {
                    boolean created = dataDir.mkdirs();
                    System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
                }
            }

            System.out.println("ReviewManager: Using data file path: " + this.dataFilePath);
        }


    }
