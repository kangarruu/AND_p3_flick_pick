package com.example.and_p3_flick_pick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

    @SerializedName("id")
    private int movieId;                        //movie id
    private String key;                         //video key for youtube
    private String name;                        //title of video
    private String type;                        //type of video

    public Video(int movieId, String key, String name, String type) {
        this.movieId = movieId;
        this.key = key;
        this.name = name;
        this.type = type;

    }

    protected Video(Parcel in) {
        movieId = in.readInt();
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    //Getters
    public int getMovieId() {
        return movieId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(type);
    }



    @Override
    public String toString() {
        return "Video{" +
                "movieId=" + movieId +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
