package com.example.moviecataloguefinalproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbmovie";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.MovieColumns.TABLE_FAVORITES,
            DatabaseContract.MovieColumns._ID,
            DatabaseContract.MovieColumns.TITLE,
            DatabaseContract.MovieColumns.DESCRIPTION,
            DatabaseContract.MovieColumns.VOTES,
            DatabaseContract.MovieColumns.RELEASE_DATE,
            DatabaseContract.MovieColumns.POSTER_PATH

    );

    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TvColumns.TABLE_FAVORITES,
            DatabaseContract.TvColumns._ID,
            DatabaseContract.TvColumns.TITLE,
            DatabaseContract.TvColumns.DESCRIPTION,
            DatabaseContract.TvColumns.VOTES,
            DatabaseContract.TvColumns.RELEASE_DATE,
            DatabaseContract.TvColumns.POSTER_PATH

    );


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MovieColumns.TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TvColumns.TABLE_FAVORITES);
        onCreate(db);
    }
}
