package com.example.moviecataloguefinalproject.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.database.DatabaseContract;
import com.example.moviecataloguefinalproject.database.OperationHelper;
import com.example.moviecataloguefinalproject.model.ListData;
import com.example.moviecataloguefinalproject.widget.FavoriteMovieWidget;

public class DetailsActivityMovie extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MOVIE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private ListData movieFav;
    private int position;

    private static boolean isInsert = false;

    private OperationHelper movieFavHelper;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;

    TextView tvTitleMovFav;
    TextView tvReleaseDateMovFav;
    TextView tvVoteAverageMovFav;
    TextView tvOverviewMovFav;
    TextView tvUrlMovFav;
    ImageView imageView;
    Button btnAddMov;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        tvTitleMovFav = findViewById(R.id.title);
        tvReleaseDateMovFav = findViewById(R.id.release_date);
        tvVoteAverageMovFav = findViewById(R.id.vote);
        tvOverviewMovFav = findViewById(R.id.description);
        imageView = findViewById(R.id.img_item_photo);
        tvUrlMovFav = findViewById(R.id.tv_url_image_mov);

        btnAddMov = findViewById(R.id.btn_add_favorite);
        btnAddMov.setOnClickListener(this);

        movieFavHelper = OperationHelper.getInstance(getApplicationContext());
        movieFavHelper.open();

        movieFav = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movieFav != null){
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isInsert  = true;

            tvTitleMovFav.setText(movieFav.getTitle());
            tvReleaseDateMovFav.setText(movieFav.getReleaseDate());
            tvVoteAverageMovFav.setText(movieFav.getRatings());
            tvOverviewMovFav.setText(movieFav.getDescription());
            tvUrlMovFav.setText(movieFav.getPhoto());

            Glide.with(DetailsActivityMovie.this)
                    .load(movieFav.getPhoto())
                    .placeholder(R.color.colorAccent)
                    .override(50, 75)
                    .into(imageView);
        } else {
            movieFav = new ListData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_favorite) {

            String titles = tvTitleMovFav.getText().toString().trim();
            String overview = tvOverviewMovFav.getText().toString().trim();
            String release_date = tvReleaseDateMovFav.getText().toString().trim();
            String vote_average = tvVoteAverageMovFav.getText().toString().trim();

            String url_poster = tvUrlMovFav.getText().toString().trim();

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.MovieColumns._ID, movieFav.getId());
            values.put(DatabaseContract.MovieColumns.TITLE, titles);
            values.put(DatabaseContract.MovieColumns.DESCRIPTION, overview);
            values.put(DatabaseContract.MovieColumns.RELEASE_DATE, release_date);
            values.put(DatabaseContract.MovieColumns.VOTES, vote_average);
            values.put(DatabaseContract.MovieColumns.POSTER_PATH, url_poster);

            if (isInsert) {
                Uri result = getContentResolver().insert(DatabaseContract.MovieColumns.CONTENT_URI, values);
                sendUpdate(this);
                if (result != null) {
                    Toast.makeText(DetailsActivityMovie.this, getString(R.string.success_add), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetailsActivityMovie.this, getString(R.string.failed_add), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

        }
    }

    private void sendUpdate(Context context){
        Intent intent = new Intent(context, FavoriteMovieWidget.class);
        intent.setAction(FavoriteMovieWidget.UPDATE_ITEM);
        context.sendBroadcast(intent);
    }
}
