package com.movierental.model.movie;

public class Movie {
    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", rating=" + rating +
                ", available=" + available +
                ", coverPhotoPath='" + coverPhotoPath + '\'' +
                '}';
    }
}

