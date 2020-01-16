package com.example.moviecataloguefinalproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.database.DatabaseContract;
import com.example.moviecataloguefinalproject.helper.MappingHelper;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteMovie;

public class DetailsTvFavoriteActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String SEND_MOVIE_FAVORITE = "send_movie_favorite";
    public static final String SEND_POSITION = "send_position";

    TextView tvTitleMovFav;
    TextView tvReleaseDateMovFav;
    TextView tvVoteAverageMovFav;
    TextView tvOverviewMovFav;
    ImageView imageView;
    Button btnDeleteMovFav;

    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;

    private int position;

    private Uri uriWithId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tv_favorite);

        tvTitleMovFav = findViewById(R.id.title);
        tvReleaseDateMovFav = findViewById(R.id.release_date);
        tvVoteAverageMovFav = findViewById(R.id.vote);
        tvOverviewMovFav = findViewById(R.id.description);
        imageView = findViewById(R.id.img_item_photo);

        btnDeleteMovFav = findViewById(R.id.btn_remove_favorite);
        btnDeleteMovFav.setOnClickListener(this);

        ListDataFavoriteMovie tvFav = getIntent().getParcelableExtra(SEND_MOVIE_FAVORITE);

        if (tvFav != null) {
            position = getIntent().getIntExtra(SEND_POSITION, 0);
            tvTitleMovFav.setText(tvFav.getTitle());
            tvReleaseDateMovFav.setText(tvFav.getReleaseDate());
            tvVoteAverageMovFav.setText(tvFav.getRatings());
            tvOverviewMovFav.setText(tvFav.getDescription());

            Glide.with(DetailsTvFavoriteActivity.this)
                    .load(tvFav.getPhoto())
                    .placeholder(R.color.colorAccent)
                    .override(50, 75)
                    .into(imageView);

        }
        assert tvFav != null;
        uriWithId = Uri.parse(DatabaseContract.TvColumns.CONTENT_URI + "/" + tvFav.getId());

        if (uriWithId != null){
            Cursor cursor = getContentResolver().query(uriWithId, null, null, null, null);

            if (cursor != null){
                tvFav = MappingHelper.mapCursorToObjectMovie(cursor);
                cursor.close();
            }
        }
    }


    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (!isDialogClose) {
            dialogMessage = getString(R.string.notify_ques_delete);
            dialogTitle = getString(R.string.notify_delete_mov);

            alertDialogBuilder.setTitle(dialogTitle);
            alertDialogBuilder
                    .setMessage(dialogMessage)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            long result = getContentResolver().delete(uriWithId, null, null);
                            if (result > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(SEND_POSITION, position);
                                setResult(RESULT_DELETE, intent);
                                finish();
                            } else {
                                Toast.makeText(DetailsTvFavoriteActivity.this, getString(R.string.notify_failed_delete_data), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_remove_favorite) {
            int ALERT_DIALOG_DELETE = 20;
            showAlertDialog(ALERT_DIALOG_DELETE);
        }

    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

}
