package com.example.moviecataloguefinalproject.activity;

import android.content.ContentValues;
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

public class DetailsActivityTvShow extends AppCompatActivity implements View.OnClickListener {

    TextView tvTitleMovFav;
    TextView tvReleaseDateMovFav;
    TextView tvVoteAverageMovFav;
    TextView tvOverviewMovFav;
    TextView tvUrlMovFav;
    ImageView imageView;
    Button btnAddMov;


    public static final String EXTRA_MOVIE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private ListData tvFav;

    private boolean isInsert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tv_show);

        tvTitleMovFav = findViewById(R.id.title);
        tvReleaseDateMovFav = findViewById(R.id.release_date);
        tvVoteAverageMovFav = findViewById(R.id.vote);
        tvOverviewMovFav = findViewById(R.id.description);
        imageView = findViewById(R.id.img_item_photo);
        tvUrlMovFav = findViewById(R.id.tv_url_image_mov);

        btnAddMov = findViewById(R.id.btn_add_favorite);

        btnAddMov.setOnClickListener(this);

        OperationHelper movieFavHelper = OperationHelper.getInstance(getApplicationContext());
        movieFavHelper.open();

        tvFav = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (tvFav != null) {
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isInsert  = true;


            tvTitleMovFav.setText(tvFav.getTitle());
            tvReleaseDateMovFav.setText(tvFav.getReleaseDate());
            tvVoteAverageMovFav.setText(tvFav.getRatings());
            tvOverviewMovFav.setText(tvFav.getDescription());
            tvUrlMovFav.setText(tvFav.getPhoto());

            Glide.with(DetailsActivityTvShow.this)
                    .load(tvFav.getPhoto())
                    .placeholder(R.color.colorAccent)
                    .override(50, 75)
                    .into(imageView);

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
            values.put(DatabaseContract.TvColumns._ID, tvFav.getId());
            values.put(DatabaseContract.TvColumns.TITLE, titles);
            values.put(DatabaseContract.TvColumns.DESCRIPTION, overview);
            values.put(DatabaseContract.TvColumns.RELEASE_DATE, release_date);
            values.put(DatabaseContract.TvColumns.VOTES, vote_average);
            values.put(DatabaseContract.TvColumns.POSTER_PATH, url_poster);

            if (isInsert) {

                Uri result = getContentResolver().insert(DatabaseContract.TvColumns.CONTENT_URI, values);
                if (result != null) {
                    Toast.makeText(DetailsActivityTvShow.this, getString(R.string.success_add), Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Toast.makeText(DetailsActivityTvShow.this, getString(R.string.failed_add), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

        }
    }
}
