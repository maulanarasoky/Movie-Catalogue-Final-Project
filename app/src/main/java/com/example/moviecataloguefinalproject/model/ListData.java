package com.example.moviecataloguefinalproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class ListData implements Parcelable {
    private int id;
    private String photo, title, description, releaseDate, ratings;

    public ListData(JSONObject object, String title, String releaseDate) {
        try {
            this.id = object.getInt("id");
            this.title = object.getString(title);
            this.description = object.getString("overview");
            this.releaseDate = object.getString(releaseDate);
            this.ratings = object.getString("vote_average");
            this.photo = ("https://image.tmdb.org/t/p/w185" + object.getString("poster_path"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListData(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    protected ListData(Parcel in) {
        this.id = in.readInt();
        this.photo = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.releaseDate = in.readString();
        this.ratings = in.readString();
    }

    public static final Creator<ListData> CREATOR = new Creator<ListData>() {
        @Override
        public ListData createFromParcel(Parcel source) {
            return new ListData(source);
        }

        @Override
        public ListData[] newArray(int size) {
            return new ListData[size];
        }
    };
}
