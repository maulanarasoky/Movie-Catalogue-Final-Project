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
import com.example.moviecataloguefinalproject.activity.DetailsActivityMovie;
import com.example.moviecataloguefinalproject.model.ListData;

import java.util.ArrayList;

public class ListAdapterMovie extends RecyclerView.Adapter<ListAdapterMovie.ListViewHolder> {

    private ArrayList<ListData> listData = new ArrayList<>();

    private Activity activity;

    public ListAdapterMovie(Activity activity){
        this.activity = activity;
    }

    public void setData(ArrayList<ListData> items) {
        listData.clear();
        listData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_list, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder listViewHolder, final int position) {

        listViewHolder.title.setText(listData.get(position).getTitle());
        if (listData.get(position).getDescription().equals("[]") || listData.get(position).getDescription().equals("null") || listData.get(position).getDescription().equals("")) {
            listViewHolder.description.setText(" - ");
        } else {
            listViewHolder.description.setText(listData.get(position).getDescription());
        }
        if (listData.get(position).getReleaseDate().equals("[]") || listData.get(position).getReleaseDate().equals("null") || listData.get(position).getReleaseDate().equals("")) {
            listViewHolder.releaseDate.setText(" - ");
        } else {
            listViewHolder.releaseDate.setText(listData.get(position).getReleaseDate());
        }
        listViewHolder.ratings.setText(listData.get(position).getRatings());
        Glide.with(listViewHolder.itemView.getContext())
                .load(listData.get(position).getPhoto())
                .apply(new RequestOptions().override(130, 220))
                .into(listViewHolder.imgPhoto);

        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listViewHolder.itemView.getContext(), DetailsActivityMovie.class);
                intent.putExtra(DetailsActivityMovie.EXTRA_MOVIE, listData.get(position));
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView title, description, releaseDate, ratings;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            title = itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.tv_description);
            releaseDate = itemView.findViewById(R.id.release_date);
            ratings = itemView.findViewById(R.id.ratings);
        }
    }
}
