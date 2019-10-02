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
import com.example.moviecataloguelocalstorage.adapter.ListAdapterTv;
import com.example.moviecataloguelocalstorage.model.ListData;
import com.example.moviecataloguelocalstorage.viewmodel.MainViewModelTv;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {


    ProgressBar progressBar;
    ListAdapterTv listAdapterTv;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);


        MainViewModelTv mainViewModelTv = ViewModelProviders.of(getActivity()).get(MainViewModelTv.class);
        if (Locale.getDefault().getDisplayLanguage().toString().equals("Indonesia")) {
            mainViewModelTv.setData("id");
        } else if (Locale.getDefault().getDisplayLanguage().toString().equals("English")) {
            mainViewModelTv.setData("en");
        }
        showLoading(true);
        mainViewModelTv.getData().observe(this, getTv);

        listAdapterTv = new ListAdapterTv(getActivity());
        listAdapterTv.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listAdapterTv);
    }

    private Observer<ArrayList<ListData>> getTv = new Observer<ArrayList<ListData>>() {
        @Override
        public void onChanged(@Nullable ArrayList<ListData> listItems) {
            if (listItems != null) {
                listAdapterTv.setData(listItems);
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
