package com.example.moviecataloguefinalproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListDataFavoriteMovie implements Parcelable {
    private int id;
    private String photo, title, description, releaseDate, ratings;

    public ListDataFavoriteMovie(int id, String title, String description, String releaseDate, String ratings, String photo){
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.ratings = ratings;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.photo);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.releaseDate);
        dest.writeString(this.ratings);
    }

    public ListDataFavoriteMovie() {
    }

    protected ListDataFavoriteMovie(Parcel in) {
        this.id = in.readInt();
        this.photo = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.releaseDate = in.readString();
        this.ratings = in.readString();
    }

    public static final Creator<ListDataFavoriteMovie> CREATOR = new Creator<ListDataFavoriteMovie>() {
        @Override
        public ListDataFavoriteMovie createFromParcel(Parcel source) {
            return new ListDataFavoriteMovie(source);
        }

        @Override
        public ListDataFavoriteMovie[] newArray(int size) {
            return new ListDataFavoriteMovie[size];
        }
    };
}
