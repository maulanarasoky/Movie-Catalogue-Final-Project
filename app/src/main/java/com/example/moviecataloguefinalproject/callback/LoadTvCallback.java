package com.example.moviecataloguefinalproject.callback;

import com.example.moviecataloguefinalproject.model.ListDataFavoriteTv;

import java.util.ArrayList;

public interface LoadTvCallback {
    void preExecute();

    void postExecute(ArrayList<ListDataFavoriteTv> listDataFavoriteTv);
}
