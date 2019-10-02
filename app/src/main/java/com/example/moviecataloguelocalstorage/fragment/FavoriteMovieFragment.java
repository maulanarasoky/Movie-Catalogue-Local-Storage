package com.example.moviecataloguelocalstorage.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviecataloguelocalstorage.R;
import com.example.moviecataloguelocalstorage.activity.DetailsActivityMovie;
import com.example.moviecataloguelocalstorage.activity.DetailsMovieFavoriteActivity;
import com.example.moviecataloguelocalstorage.adapter.ListAdapterFavoriteMovie;
import com.example.moviecataloguelocalstorage.callback.LoadMovieCallback;
import com.example.moviecataloguelocalstorage.database.OperationHelper;
import com.example.moviecataloguelocalstorage.model.ListDataFavoriteMovie;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadMovieCallback {

    private static final String SEND_STATE = "SEND_STATE";
    private OperationHelper movieFavHelper;
    private RecyclerView rvMovFav;
    private ProgressBar progressBar;
    private ListAdapterFavoriteMovie moviesFavAdapter;

    Activity activity;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovFav = view.findViewById(R.id.recyclerView);
        rvMovFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovFav.setHasFixedSize(true);

        movieFavHelper = OperationHelper.getInstance(getActivity().getApplicationContext());
        movieFavHelper.open();

        progressBar = view.findViewById(R.id.progressBar);

        moviesFavAdapter = new ListAdapterFavoriteMovie(getActivity());
        rvMovFav.setAdapter(moviesFavAdapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(movieFavHelper, this).execute();
        } else {
            ArrayList<ListDataFavoriteMovie> movieFavArrayList = savedInstanceState.getParcelableArrayList(SEND_STATE);
            if (movieFavArrayList != null) {
                moviesFavAdapter.setListDataFavoriteMovies(movieFavArrayList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SEND_STATE, moviesFavAdapter.getListDataFavoriteMovies());
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies) {
        progressBar.setVisibility(View.GONE);
        moviesFavAdapter.setListDataFavoriteMovies(listDataFavoriteMovies);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieFavHelper.close();
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<ListDataFavoriteMovie>> {

        private final WeakReference<OperationHelper> weakOperationHelper;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(OperationHelper operationHelper, LoadMovieCallback callback) {
            weakOperationHelper = new WeakReference<>(operationHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<ListDataFavoriteMovie> doInBackground(Void... voids) {
            return weakOperationHelper.get().getAllMoviesFav();
        }

        @Override
        protected void onPostExecute(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies) {
            super.onPostExecute(listDataFavoriteMovies);
            weakCallback.get().postExecute(listDataFavoriteMovies);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailsMovieFavoriteActivity.REQUEST_UPDATE) {
                if (resultCode == DetailsMovieFavoriteActivity.RESULT_DELETE) {
                    int pos = data.getIntExtra(DetailsMovieFavoriteActivity.SEND_POSITION, 0);
                    moviesFavAdapter.removeItem(pos);
                    showSnackbarMessage(getString(R.string.notify_delete_mov));
                }
            }
        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvMovFav, message, Snackbar.LENGTH_SHORT).show();
    }
}
