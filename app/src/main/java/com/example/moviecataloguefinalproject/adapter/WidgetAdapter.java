package com.example.moviecataloguefinalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.database.DatabaseContract;
import com.example.moviecataloguefinalproject.widget.FavoriteMovieWidget;

import java.util.concurrent.ExecutionException;

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {


    private Context context;
    private Cursor cursor;

    private String title;


    public WidgetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(DatabaseContract.MovieColumns.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        if (cursor.moveToPosition(position)) {
            title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH));
            Bitmap bitmap;
            try {
                bitmap = Glide.with(context)
                        .asBitmap()
                        .load(poster)
                        .submit()
                        .get();

                remoteViews.setImageViewBitmap(R.id.image_widget, bitmap);
            } catch (InterruptedException | ExecutionException e) {
                Log.d("Failed to load widget", "Error");
            }
        }
        Bundle extras = new Bundle();
        extras.putString(FavoriteMovieWidget.EXTRA_ITEM, title);
        Intent intent = new Intent();
        intent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.image_widget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
