package com.movierental.model.movie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MovieManager {

    public boolean addMovie(Movie movie, InputStream coverPhotoStream, String originalFileName) {
        // Generate a unique ID if not provided
        if (movie.getMovieId() == null || movie.getMovieId().isEmpty()) {
            movie.setMovieId(UUID.randomUUID().toString());
        }

        // Save the cover photo if provided
        if (coverPhotoStream != null) {
            String fileExtension = getFileExtension(originalFileName);
            String photoFileName = movie.getMovieId() + fileExtension;
            String photoPath = imagesDirectoryPath + File.separator + photoFileName;

            try {
                savePhoto(coverPhotoStream, photoPath);
                movie.setCoverPhotoPath(MOVIE_IMAGES_DIR + "/" + photoFileName);
            } catch (IOException e) {
                System.err.println("Error saving cover photo: " + e.getMessage());
                // Continue adding the movie even if photo upload fails
            }
        }







}