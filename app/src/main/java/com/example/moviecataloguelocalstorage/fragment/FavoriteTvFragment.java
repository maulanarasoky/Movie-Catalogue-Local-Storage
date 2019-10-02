package com.example.moviecataloguelocalstorage.fragment;


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
import com.example.moviecataloguelocalstorage.activity.DetailsTvFavoriteActivity;
import com.example.moviecataloguelocalstorage.adapter.ListAdapterFavoriteTv;
import com.example.moviecataloguelocalstorage.callback.LoadTvCallback;
import com.example.moviecataloguelocalstorage.database.OperationHelper;
import com.example.moviecataloguelocalstorage.model.ListDataFavoriteTv;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvFragment extends Fragment implements LoadTvCallback {


    private static final String SEND_STATE = "SEND_STATE";
    private OperationHelper movieFavHelper;
    private RecyclerView rvMovFav;
    private ProgressBar progressBar;
    private ListAdapterFavoriteTv tvFavAdapter;

    public FavoriteTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false);
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

        tvFavAdapter = new ListAdapterFavoriteTv(getActivity());
        rvMovFav.setAdapter(tvFavAdapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(movieFavHelper, this).execute();
        } else {
            ArrayList<ListDataFavoriteTv> movieFavArrayList = savedInstanceState.getParcelableArrayList(SEND_STATE);
            if (movieFavArrayList != null) {
                tvFavAdapter.setListDataFavoriteTvs(movieFavArrayList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SEND_STATE, tvFavAdapter.getListDataFavoriteTvs());
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
    public void postExecute(ArrayList<ListDataFavoriteTv> listDataFavoriteTv) {
        progressBar.setVisibility(View.GONE);
        tvFavAdapter.setListDataFavoriteTvs(listDataFavoriteTv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieFavHelper.close();
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<ListDataFavoriteTv>> {

        private final WeakReference<OperationHelper> weakOperationHelper;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadMovieAsync(OperationHelper operationHelper, LoadTvCallback callback) {
            weakOperationHelper = new WeakReference<>(operationHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<ListDataFavoriteTv> doInBackground(Void... voids) {
            return weakOperationHelper.get().getAllTvFav();
        }

        @Override
        protected void onPostExecute(ArrayList<ListDataFavoriteTv> listDataFavoriteTv) {
            super.onPostExecute(listDataFavoriteTv);
            weakCallback.get().postExecute(listDataFavoriteTv);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (requestCode == DetailsTvFavoriteActivity.REQUEST_UPDATE) {
                if (resultCode == DetailsTvFavoriteActivity.RESULT_DELETE) {
                    int pos = data.getIntExtra(DetailsTvFavoriteActivity.SEND_POSITION, 0);
                    tvFavAdapter.removeItem(pos);
                    showSnackbarMessage(getString(R.string.notify_delete_mov));
                }
            }
        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvMovFav, message, Snackbar.LENGTH_SHORT).show();
    }
}
