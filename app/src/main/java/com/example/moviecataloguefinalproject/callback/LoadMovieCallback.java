package com.example.moviecataloguefinalproject.callback;

import com.example.moviecataloguefinalproject.model.ListDataFavoriteMovie;

import java.util.ArrayList;

public interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies);
}
