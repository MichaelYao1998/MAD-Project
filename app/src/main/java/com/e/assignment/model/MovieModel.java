package com.e.assignment.model;

import java.util.Map;

public interface MovieModel {
    Map<String, Movie> getMovieList();
    Movie getMovieById(String MovieId);
}
