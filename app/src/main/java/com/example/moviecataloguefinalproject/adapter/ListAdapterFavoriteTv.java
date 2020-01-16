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
import com.example.moviecataloguefinalproject.activity.DetailsTvFavoriteActivity;
import com.example.moviecataloguefinalproject.model.ListDataFavoriteTv;

import java.util.ArrayList;

public class ListAdapterFavoriteTv extends RecyclerView.Adapter<ListAdapterFavoriteTv.ViewHolder> {

    private ArrayList<ListDataFavoriteTv> listDataFavoriteTvs = new ArrayList<>();
    private Activity activity;

    public ListAdapterFavoriteTv(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ListDataFavoriteTv> getListDataFavoriteTvs() {
        return listDataFavoriteTvs;
    }

    public void setListDataFavoriteTvs(ArrayList<ListDataFavoriteTv> listDataFavoriteTvs) {
        if (listDataFavoriteTvs.size() > 0) {
            this.listDataFavoriteTvs.clear();
        }
        this.listDataFavoriteTvs.addAll(listDataFavoriteTvs);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listDataFavoriteTvs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.listDataFavoriteTvs.size());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_list, parent, false);
        return new ListAdapterFavoriteTv.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListDataFavoriteTv listDataFavoriteTv = listDataFavoriteTvs.get(position);
        holder.title.setText(listDataFavoriteTvs.get(position).getTitle());
        holder.description.setText(listDataFavoriteTvs.get(position).getDescription());
        holder.releaseDate.setText(listDataFavoriteTvs.get(position).getReleaseDate());
        holder.ratings.setText(listDataFavoriteTvs.get(position).getRatings());
        Glide.with(holder.itemView.getContext())
                .load(listDataFavoriteTvs.get(position).getPhoto())
                .apply(new RequestOptions().override(130, 220))
                .into(holder.imgPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsTvFavoriteActivity.class);
                intent.putExtra(DetailsTvFavoriteActivity.SEND_MOVIE_FAVORITE, listDataFavoriteTvs.get(position));
                intent.putExtra(DetailsTvFavoriteActivity.SEND_POSITION, position);
                intent.putExtra(DetailsTvFavoriteActivity.SEND_MOVIE_FAVORITE, listDataFavoriteTv);
                activity.startActivityForResult(intent, DetailsTvFavoriteActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDataFavoriteTvs.size();
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
