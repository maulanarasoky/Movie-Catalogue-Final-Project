package com.example.moviecataloguefinalproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.fragment.FavoriteMovieFragment;
import com.example.moviecataloguefinalproject.fragment.FavoriteTvFragment;
import com.example.moviecataloguefinalproject.fragment.MovieFragment;
import com.example.moviecataloguefinalproject.fragment.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    String condition;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.movie:
                    condition = "movie";
                    fragment = new MovieFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.tvShow:
                    condition = "tv";
                    fragment = new TvShowFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;

                case R.id.favorite_movie:
                    condition = "movie";
                    fragment = new FavoriteMovieFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;

                case R.id.favorite_tv:
                    condition = "tv";
                    fragment = new FavoriteTvFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.movie);
        }
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.movie != seletedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.search) {
            if (condition.equals("movie")) {
                intent = new Intent(this, SearchMovieActivity.class);
                startActivity(intent);
            } else if (condition.equals("tv")) {
                intent = new Intent(this, SearchTvActivity.class);
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.reminder) {
            intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
