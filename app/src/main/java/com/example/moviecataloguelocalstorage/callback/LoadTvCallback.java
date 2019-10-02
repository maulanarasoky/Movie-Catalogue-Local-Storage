package com.example.moviecataloguelocalstorage.callback;

import com.example.moviecataloguelocalstorage.model.ListDataFavoriteTv;

import java.util.ArrayList;

public interface LoadTvCallback {
    void preExecute();

    void postExecute(ArrayList<ListDataFavoriteTv> listDataFavoriteTv);
}
