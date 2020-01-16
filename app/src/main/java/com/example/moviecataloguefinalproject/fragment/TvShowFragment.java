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
import com.example.moviecataloguefinalproject.adapter.ListAdapterTv;
import com.example.moviecataloguefinalproject.model.ListData;
import com.example.moviecataloguefinalproject.viewmodel.MainViewModelTv;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {


    private ProgressBar progressBar;
    private ListAdapterTv listAdapterTv;

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


        MainViewModelTv mainViewModelTv = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModelTv.class);
        if (Locale.getDefault().getDisplayLanguage().equals("Indonesia")) {
            mainViewModelTv.setData("id");
        } else if (Locale.getDefault().getDisplayLanguage().equals("English")) {
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
