package com.example.moviecataloguelocalstorage.callback;

import com.example.moviecataloguelocalstorage.model.ListDataFavoriteMovie;

import java.util.ArrayList;

public interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies);
}
