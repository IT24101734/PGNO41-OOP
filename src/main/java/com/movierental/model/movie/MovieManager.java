package com.movierental.model.movie;

import java.util.List;

public class MovieManager {
    public class MovieManager {
        // Changed from a hardcoded path to a dynamic one
        private static final String MOVIE_FILE_NAME = "movies.txt";
        private static final String MOVIE_IMAGES_DIR = "movie_images";

        private List<Movie> movies;
        private ServletContext servletContext;
        private String dataFilePath;
        private String imagesDirectoryPath;

    }
