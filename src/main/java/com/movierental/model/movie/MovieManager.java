package com.movierental.model.movie;

import java.util.ArrayList;
import java.util.List;

public class MovieManager{

    public List<Movie> getAvailableMovies() {
        List<Movie> results = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.isAvailable()) {
                results.add(movie);


            }