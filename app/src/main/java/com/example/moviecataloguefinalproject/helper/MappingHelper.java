package com.example.moviecataloguefinalproject.helper;

import android.database.Cursor;

import com.example.moviecataloguefinalproject.database.DatabaseContract;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteMovie;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteTv;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<ListDataFavoriteMovie> mapCursorToArrayListMovie(Cursor notesCursor) {
        ArrayList<ListDataFavoriteMovie> notesList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION));
            String votes = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTES));
            String releaseDate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE));
            String posterPath = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH));
            notesList.add(new ListDataFavoriteMovie(id, title, description, releaseDate, votes, posterPath));
        }

        return notesList;
    }

    public static ListDataFavoriteMovie mapCursorToObjectMovie(Cursor notesCursor) {
        notesCursor.moveToFirst();
        int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
        String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
        String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION));
        String votes = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTES));
        String releaseDate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE));
        String posterPath = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH));

        return new ListDataFavoriteMovie(id, title, description, releaseDate, votes, posterPath);
    }

    public static ArrayList<ListDataFavoriteTv> mapCursorToArrayListTv(Cursor notesCursor) {
        ArrayList<ListDataFavoriteTv> notesList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns._ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.DESCRIPTION));
            String votes = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.VOTES));
            String releaseDate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.RELEASE_DATE));
            String posterPath = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.POSTER_PATH));
            notesList.add(new ListDataFavoriteTv(id, title, description, releaseDate, votes, posterPath));
        }

        return notesList;
    }
}
