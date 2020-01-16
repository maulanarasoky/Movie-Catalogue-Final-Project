package com.example.moviecataloguefinalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.adapter.ListAdapterMovie;
import com.example.moviecataloguefinalproject.model.ListData;
import com.example.moviecataloguefinalproject.viewmodel.MainViewModelSearchMovie;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SearchMovieActivity extends AppCompatActivity {

    EditText searchBar;
    ProgressBar progressBar;
    ListAdapterMovie listAdapterMovie;
    Button btnSearch;

    private String language;

    RecyclerView recyclerView;

    private MainViewModelSearchMovie mainViewModelSearchMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        setActionBarTitle();

        searchBar = findViewById(R.id.editMovie);
        progressBar = findViewById(R.id.progressBar);
        btnSearch = findViewById(R.id.btnMovie);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapterMovie = new ListAdapterMovie(this);
        listAdapterMovie.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapterMovie);

        if (Locale.getDefault().getDisplayLanguage().equals("Indonesia")) {
           language = "id";
        } else if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            language = "en";
        }

        mainViewModelSearchMovie = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModelSearchMovie.class);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movie = searchBar.getText().toString().trim();

                if (TextUtils.isEmpty(movie)) return;


                mainViewModelSearchMovie.setData(language, movie);
                showLoading(true);
            }
        });

        mainViewModelSearchMovie.getData().observe(this, new Observer<ArrayList<ListData>>() {
            @Override
            public void onChanged(ArrayList<ListData> listData) {
                if (listData != null){
                    listAdapterMovie.setData(listData);
                    showLoading(false);
                }else {
                    showSnackbarMessage();
                }
            }
        });
    }

    private void showSnackbarMessage() {
        Snackbar.make(recyclerView, "Not found", Snackbar.LENGTH_SHORT).show();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setActionBarTitle(){
        Objects.requireNonNull(getSupportActionBar()).setTitle("Search Movie");
    }
}
