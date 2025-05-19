package com.movierental.model.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

private void loadMovies() {
    File file = new File(dataFilePath);

    // Create directory if it doesn't exist
    if (file.getParentFile() != null) {
        file.getParentFile().mkdirs();
    }

    if (!file.exists()) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating movies file: " + e.getMessage());
        }
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            Movie movie = null;
            if (line.startsWith("NEW_RELEASE,")) {
                movie = NewRelease.fromFileString(line);
            } else if (line.startsWith("CLASSIC,")) {
                movie = ClassicMovie.fromFileString(line);
            } else if (line.startsWith("REGULAR,")) {
                movie = Movie.fromFileString(line);
            }

            if (movie != null) {
                movies.add(movie);
            }
        }
    } catch (IOException e) {
        System.err.println("Error loading movies: " + e.getMessage());
    }
}

