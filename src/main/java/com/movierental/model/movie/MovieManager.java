package com.movierental.model.movie;

public class MovieManager{


public boolean updateMovie(Movie updatedMovie, InputStream coverPhotoStream, String originalFileName) {
    for (int i = 0; i < movies.size(); i++) {
        if (movies.get(i).getMovieId().equals(updatedMovie.getMovieId())) {
            // If a new cover photo is provided, save it and update the path
            if (coverPhotoStream != null) {
                String fileExtension = getFileExtension(originalFileName);
                String photoFileName = updatedMovie.getMovieId() + fileExtension;
                String photoPath = imagesDirectoryPath + File.separator + photoFileName;

                try {
                    savePhoto(coverPhotoStream, photoPath);
                    updatedMovie.setCoverPhotoPath(MOVIE_IMAGES_DIR + "/" + photoFileName);
                } catch (IOException e) {
                    System.err.println("Error updating cover photo: " + e.getMessage());
                    // Continue updating the movie even if photo update fails
                    // Keep the old photo path
                    updatedMovie.setCoverPhotoPath(movies.get(i).getCoverPhotoPath());
                }
            } else {
                // No new photo provided, keep the old one
                updatedMovie.setCoverPhotoPath(movies.get(i).getCoverPhotoPath());
            }

            movies.set(i, updatedMovie);
            saveMovies();
            return true;
        }
    }
    return false;
}





}