package com.example.moviecataloguefinalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moviecataloguefinalproject.model.ListDataFavoriteMovie;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteTv;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class OperationHelper {

    private DatabaseHelper dataBaseHelper;
    private static OperationHelper INSTANCE;

    private SQLiteDatabase database;

    public OperationHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public static OperationHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OperationHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public ArrayList<ListDataFavoriteMovie> getAllMoviesFav() {
        ArrayList<ListDataFavoriteMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseContract.MovieColumns.TABLE_FAVORITES, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        ListDataFavoriteMovie movieFavorite;
        if (cursor.getCount() > 0) {
            do {
                movieFavorite = new ListDataFavoriteMovie();
                movieFavorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieFavorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE)));
                movieFavorite.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION)));
                movieFavorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE)));
                movieFavorite.setRatings(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTES)));
                movieFavorite.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH)));

                arrayList.add(movieFavorite);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryAllFavMovie(){
        return database.query(DatabaseContract.MovieColumns.TABLE_FAVORITES, null,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public ArrayList<ListDataFavoriteTv> getAllTvFav() {
        ArrayList<ListDataFavoriteTv> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseContract.TvColumns.TABLE_FAVORITES, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        ListDataFavoriteTv listDataFavoriteTv;
        if (cursor.getCount() > 0) {
            do {
                listDataFavoriteTv = new ListDataFavoriteTv();
                listDataFavoriteTv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                listDataFavoriteTv.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.TITLE)));
                listDataFavoriteTv.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.DESCRIPTION)));
                listDataFavoriteTv.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.RELEASE_DATE)));
                listDataFavoriteTv.setRatings(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.VOTES)));
                listDataFavoriteTv.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.POSTER_PATH)));

                arrayList.add(listDataFavoriteTv);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public int deleteTv(int id) {
        return database.delete(DatabaseContract.TvColumns.TABLE_FAVORITES, _ID + " = '" + id + "'", null);
    }

    public Cursor queryAllFavMovieById(String id){
        return database.query(DatabaseContract.TvColumns.TABLE_FAVORITES, null,
                _ID + " = ? ",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insertContentProviderMovie(ContentValues values){
        return database.insert(DatabaseContract.MovieColumns.TABLE_FAVORITES, null, values);
    }

    public Cursor queryByIdProviderMovie(String id){
        return database.query(DatabaseContract.MovieColumns.TABLE_FAVORITES,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProviderMovie(){
        return database.query(DatabaseContract.MovieColumns.TABLE_FAVORITES
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }

    public int updateContentProviderMovie(String id, ContentValues values){
        return database.update(DatabaseContract.MovieColumns.TABLE_FAVORITES, values, _ID + " = ?", new String[]{id});
    }

    public int deleteContentProviderMovie(String id){
        return database.delete(DatabaseContract.MovieColumns.TABLE_FAVORITES, _ID + " = ?", new String[]{id});
    }



    public long insertContentProviderTv(ContentValues values){
        return database.insert(DatabaseContract.TvColumns.TABLE_FAVORITES, null, values);
    }

    public Cursor queryByIdProviderTv(String id){
        return database.query(DatabaseContract.TvColumns.TABLE_FAVORITES,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProviderTv(){
        return database.query(DatabaseContract.TvColumns.TABLE_FAVORITES
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }

    public int updateContentProviderTv(String id, ContentValues values){
        return database.update(DatabaseContract.TvColumns.TABLE_FAVORITES, values, _ID + " = ?", new String[]{id});
    }

    public int deleteContentProviderTv(String id){
        return database.delete(DatabaseContract.TvColumns.TABLE_FAVORITES, _ID + " = ?", new String[]{id});
    }
}
