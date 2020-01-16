package com.example.moviecataloguefinalproject.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String SCHEME = "content";

    public static final class MovieColumns implements BaseColumns {
        public static final String AUTHORITY = "com.example.moviecataloguefinalproject.movie";
        public static final String TABLE_FAVORITES = "moviefav";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String VOTES = "votes";
        public static final String RELEASE_DATE = "release_date";
        public static final String POSTER_PATH = "poster_path";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITES)
                .build();
    }

    public static final class TvColumns implements BaseColumns {
        public static final String AUTHORITY = "com.example.moviecataloguefinalproject.tv";
        public static final String TABLE_FAVORITES = "tvfav";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String VOTES = "votes";
        public static final String RELEASE_DATE = "release_date";
        public static final String POSTER_PATH = "poster_path";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITES)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
}
