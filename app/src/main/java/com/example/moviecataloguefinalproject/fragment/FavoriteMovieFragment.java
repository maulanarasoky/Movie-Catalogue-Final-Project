package com.example.moviecataloguefinalproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.activity.DetailsMovieFavoriteActivity;
import com.example.moviecataloguefinalproject.adapter.ListAdapterFavoriteMovie;
import com.example.moviecataloguefinalproject.callback.LoadMovieCallback;
import com.example.moviecataloguefinalproject.database.DatabaseContract;
import com.example.moviecataloguefinalproject.database.OperationHelper;
import com.example.moviecataloguefinalproject.helper.MappingHelper;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteMovie;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadMovieCallback {

    private static final String SEND_STATE = "SEND_STATE";
    private OperationHelper movieFavHelper;
    private RecyclerView rvMovFav;
    private ProgressBar progressBar;
    private ListAdapterFavoriteMovie moviesFavAdapter;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovFav = view.findViewById(R.id.recyclerView);
        rvMovFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovFav.setHasFixedSize(true);

        movieFavHelper = OperationHelper.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        movieFavHelper.open();

        progressBar = view.findViewById(R.id.progressBar);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();

        getActivity().getContentResolver().notifyChange(DatabaseContract.MovieColumns.CONTENT_URI, new DataObserver(new Handler(), getContext()));
        moviesFavAdapter = new ListAdapterFavoriteMovie(getActivity());
        rvMovFav.setAdapter(moviesFavAdapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            ArrayList<ListDataFavoriteMovie> movieFavArrayList = savedInstanceState.getParcelableArrayList(SEND_STATE);
            if (movieFavArrayList != null) {
                moviesFavAdapter.setListDataFavoriteMovies(movieFavArrayList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SEND_STATE, moviesFavAdapter.getListDataFavoriteMovies());
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (listDataFavoriteMovies.size() > 0){
            moviesFavAdapter.setListDataFavoriteMovies(listDataFavoriteMovies);
        }else {
            moviesFavAdapter.setListDataFavoriteMovies(new ArrayList<ListDataFavoriteMovie>());
            showSnackbarMessage("No Data");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieFavHelper.close();
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<ListDataFavoriteMovie>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<ListDataFavoriteMovie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.MovieColumns.CONTENT_URI, null, null, null, null);
            assert dataCursor != null;
            return MappingHelper.mapCursorToArrayListMovie(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies) {
            super.onPostExecute(listDataFavoriteMovies);
            weakCallback.get().postExecute(listDataFavoriteMovies);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailsMovieFavoriteActivity.REQUEST_UPDATE) {
                if (resultCode == DetailsMovieFavoriteActivity.RESULT_DELETE) {
                    int pos = data.getIntExtra(DetailsMovieFavoriteActivity.SEND_POSITION, 0);
                    moviesFavAdapter.removeItem(pos);
                    showSnackbarMessage(getString(R.string.notify_delete_mov));
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovFav, message, Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, (LoadMovieCallback) context).execute();
        }
    }
}
