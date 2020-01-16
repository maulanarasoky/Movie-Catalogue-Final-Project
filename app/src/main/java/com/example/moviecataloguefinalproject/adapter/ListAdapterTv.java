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
import com.example.moviecataloguefinalproject.activity.DetailsActivityTvShow;
import com.example.moviecataloguefinalproject.model.ListData;

import java.util.ArrayList;

public class ListAdapterTv extends RecyclerView.Adapter<ListAdapterTv.ListViewHolder> {

    private ArrayList<ListData> listDataTv = new ArrayList<>();

    private Activity activity;

    public ListAdapterTv(Activity activity){
        this.activity = activity;
    }

    public void setData(ArrayList<ListData> items) {
        listDataTv.clear();
        listDataTv.addAll(items);
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
        listViewHolder.title.setText(listDataTv.get(position).getTitle());

        if (listDataTv.get(position).getDescription().equals("[]") || listDataTv.get(position).getDescription().equals("null") || listDataTv.get(position).getDescription().equals("")) {
            listViewHolder.description.setText(" - ");
        } else {
            listViewHolder.description.setText(listDataTv.get(position).getDescription());
        }
        if (listDataTv.get(position).getReleaseDate().equals("[]") || listDataTv.get(position).getReleaseDate().equals("null") || listDataTv.get(position).getReleaseDate().equals("")) {
            listViewHolder.releaseDate.setText(" - ");
        } else {
            listViewHolder.releaseDate.setText(listDataTv.get(position).getReleaseDate());
        }
        listViewHolder.ratings.setText(listDataTv.get(position).getRatings());
        Glide.with(listViewHolder.itemView.getContext())
                .load(listDataTv.get(position).getPhoto())
                .apply(new RequestOptions().override(130, 220))
                .into(listViewHolder.imgPhoto);

        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listViewHolder.itemView.getContext(), DetailsActivityTvShow.class);
                intent.putExtra(DetailsActivityTvShow.EXTRA_MOVIE, listDataTv.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDataTv.size();
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
