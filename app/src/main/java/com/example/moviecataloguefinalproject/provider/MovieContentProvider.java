package com.example.moviecataloguefinalproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.moviecataloguefinalproject.database.OperationHelper;

import java.util.Objects;

import static com.example.moviecataloguefinalproject.database.DatabaseContract.MovieColumns.AUTHORITY;
import static com.example.moviecataloguefinalproject.database.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.moviecataloguefinalproject.database.DatabaseContract.MovieColumns.TABLE_FAVORITES;

public class MovieContentProvider extends ContentProvider {


    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private OperationHelper operationHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITES, MOVIE);

        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITES + "/#", MOVIE_ID);
    }

    public MovieContentProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted;
        if (uriMatcher.match(uri) == MOVIE_ID) {
            deleted = operationHelper.deleteContentProviderMovie(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long added;
        if (uriMatcher.match(uri) == MOVIE) {
            added = operationHelper.insertContentProviderMovie(values);
        } else {
            added = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        operationHelper = OperationHelper.getInstance(getContext());
        operationHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                cursor = operationHelper.queryProviderMovie();
                break;
            case MOVIE_ID:
                cursor = operationHelper.queryByIdProviderMovie(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null){
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int updated;
        if (uriMatcher.match(uri) == MOVIE_ID) {
            updated = operationHelper.updateContentProviderMovie(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
