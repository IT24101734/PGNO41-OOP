package com.movierental.model.review;

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


    }
