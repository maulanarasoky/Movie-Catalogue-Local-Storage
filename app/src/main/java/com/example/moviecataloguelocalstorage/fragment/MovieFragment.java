package com.example.moviecataloguelocalstorage.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviecataloguelocalstorage.R;
import com.example.moviecataloguelocalstorage.adapter.ListAdapterMovie;
import com.example.moviecataloguelocalstorage.model.ListData;
import com.example.moviecataloguelocalstorage.viewmodel.MainViewModelMovie;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    ProgressBar progressBar;
    ListAdapterMovie listAdapterMovie;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);


        MainViewModelMovie mainViewModelMovie = ViewModelProviders.of(getActivity()).get(MainViewModelMovie.class);
        if (Locale.getDefault().getDisplayLanguage().toString().equals("Indonesia")) {
            mainViewModelMovie.setData("id");
        } else if (Locale.getDefault().getDisplayLanguage().toString().equals("English")) {
            mainViewModelMovie.setData("en");
        }
        showLoading(true);
        mainViewModelMovie.getData().observe(this, getMovie);

        listAdapterMovie = new ListAdapterMovie(getActivity());
        listAdapterMovie.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listAdapterMovie);

    }

    private Observer<ArrayList<ListData>> getMovie = new Observer<ArrayList<ListData>>() {
        @Override
        public void onChanged(@Nullable ArrayList<ListData> listItems) {
            if (listItems != null) {
                listAdapterMovie.setData(listItems);
                showLoading(false);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
