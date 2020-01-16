package com.example.moviecataloguefinalproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.activity.DetailsMovieFavoriteActivity;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteMovie;

import java.util.ArrayList;

public class ListAdapterFavoriteMovie extends RecyclerView.Adapter<ListAdapterFavoriteMovie.ViewHolder> {

    private ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies = new ArrayList<>();
    private Activity activity;

    public ListAdapterFavoriteMovie(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ListDataFavoriteMovie> getListDataFavoriteMovies() {
        return listDataFavoriteMovies;
    }

    public void setListDataFavoriteMovies(ArrayList<ListDataFavoriteMovie> listDataFavoriteMovies) {
        if (listDataFavoriteMovies.size() > 0) {
            this.listDataFavoriteMovies.clear();
        }
        this.listDataFavoriteMovies.addAll(listDataFavoriteMovies);
        notifyDataSetChanged();
    }

    public void addItem(ListDataFavoriteMovie listDataFavoriteMovie) {
        this.listDataFavoriteMovies.add(listDataFavoriteMovie);
        notifyItemInserted(this.listDataFavoriteMovies.size() - 1);
    }

    public void removeItem(int position) {
        this.listDataFavoriteMovies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.listDataFavoriteMovies.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_list, parent, false);
        return new ListAdapterFavoriteMovie.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListDataFavoriteMovie listDataFavoriteMovie = listDataFavoriteMovies.get(position);
        holder.title.setText(listDataFavoriteMovies.get(position).getTitle());
        holder.description.setText(listDataFavoriteMovies.get(position).getDescription());
        holder.releaseDate.setText(listDataFavoriteMovies.get(position).getReleaseDate());
        holder.ratings.setText((listDataFavoriteMovies.get(position).getRatings() + " Votes"));
        Glide.with(holder.itemView.getContext())
                .load(listDataFavoriteMovies.get(position).getPhoto())
                .apply(new RequestOptions().override(130, 220))
                .into(holder.imgPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsMovieFavoriteActivity.class);
                intent.putExtra(DetailsMovieFavoriteActivity.SEND_MOVIE_FAVORITE, listDataFavoriteMovies.get(position));
                intent.putExtra(DetailsMovieFavoriteActivity.SEND_POSITION, position);
                intent.putExtra(DetailsMovieFavoriteActivity.SEND_MOVIE_FAVORITE, listDataFavoriteMovie);
                activity.startActivityForResult(intent, DetailsMovieFavoriteActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDataFavoriteMovies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView title, description, releaseDate, ratings;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            title = itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.tv_description);
            releaseDate = itemView.findViewById(R.id.release_date);
            ratings = itemView.findViewById(R.id.ratings);
        }
    }
}
