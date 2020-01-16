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
import com.example.moviecataloguefinalproject.activity.DetailsTvFavoriteActivity;
import com.example.moviecataloguefinalproject.adapter.ListAdapterFavoriteTv;
import com.example.moviecataloguefinalproject.callback.LoadTvCallback;
import com.example.moviecataloguefinalproject.database.DatabaseContract;
import com.example.moviecataloguefinalproject.database.OperationHelper;
import com.example.moviecataloguefinalproject.helper.MappingHelper;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteTv;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvFragment extends Fragment implements LoadTvCallback {


    private static final String SEND_STATE = "SEND_STATE";
    private OperationHelper tvFavHelper;
    private RecyclerView rvTvFav;
    private ProgressBar progressBar;
    private ListAdapterFavoriteTv tvFavAdapter;

    public FavoriteTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTvFav = view.findViewById(R.id.recyclerView);
        rvTvFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTvFav.setHasFixedSize(true);

        tvFavHelper = OperationHelper.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        tvFavHelper.open();

        progressBar = view.findViewById(R.id.progressBar);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();

        getActivity().getContentResolver().notifyChange(DatabaseContract.TvColumns.CONTENT_URI, new DataObserver(new Handler(), getContext()));
        tvFavAdapter = new ListAdapterFavoriteTv(getActivity());
        rvTvFav.setAdapter(tvFavAdapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            ArrayList<ListDataFavoriteTv> tvFavArrayList = savedInstanceState.getParcelableArrayList(SEND_STATE);
            if (tvFavArrayList != null) {
                tvFavAdapter.setListDataFavoriteTvs(tvFavArrayList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SEND_STATE, tvFavAdapter.getListDataFavoriteTvs());
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<ListDataFavoriteTv> listDataFavoriteTv) {
        progressBar.setVisibility(View.INVISIBLE);
        if (listDataFavoriteTv.size() > 0){
            tvFavAdapter.setListDataFavoriteTvs(listDataFavoriteTv);
        }else {
            tvFavAdapter.setListDataFavoriteTvs(new ArrayList<ListDataFavoriteTv>());
            showSnackbarMessage("No Data");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tvFavHelper.close();
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<ListDataFavoriteTv>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadTvCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<ListDataFavoriteTv> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.TvColumns.CONTENT_URI, null, null, null, null);
            assert dataCursor != null;
            return MappingHelper.mapCursorToArrayListTv(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<ListDataFavoriteTv> listDataFavoriteTv) {
            super.onPostExecute(listDataFavoriteTv);
            weakCallback.get().postExecute(listDataFavoriteTv);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailsTvFavoriteActivity.REQUEST_UPDATE) {
                if (resultCode == DetailsTvFavoriteActivity.RESULT_DELETE) {
                    int pos = data.getIntExtra(DetailsTvFavoriteActivity.SEND_POSITION, 0);
                    tvFavAdapter.removeItem(pos);
                    showSnackbarMessage(getString(R.string.notify_delete_mov));
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvTvFav, message, Snackbar.LENGTH_SHORT).show();
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
            new LoadMovieAsync(context, (LoadTvCallback) context).execute();
        }
    }
}
