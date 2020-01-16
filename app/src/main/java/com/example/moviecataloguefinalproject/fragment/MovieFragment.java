package com.example.moviecataloguefinalproject.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.adapter.ListAdapterMovie;
import com.example.moviecataloguefinalproject.model.ListData;
import com.example.moviecataloguefinalproject.viewmodel.MainViewModelMovie;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    private ProgressBar progressBar;
    private ListAdapterMovie listAdapterMovie;

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


        MainViewModelMovie mainViewModelMovie = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModelMovie.class);
        if (Locale.getDefault().getDisplayLanguage().equals("Indonesia")) {
            mainViewModelMovie.setData("id");
        } else if (Locale.getDefault().getDisplayLanguage().equals("English")) {
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
