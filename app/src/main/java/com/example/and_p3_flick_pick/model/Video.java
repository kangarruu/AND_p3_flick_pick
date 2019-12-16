package com.example.and_p3_flick_pick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Video implements Parcelable {

    @SerializedName("key")
    @Expose
    private String key;                         //video key for youtube

    @SerializedName("name")
    @Expose
    private String name;                        //title of video

    @SerializedName("type")
    @Expose
    private String type;                        //type of video


    public Video(String key, String name, String type) {
        this.key = key;
        this.name = name;
        this.type = type;

    }

    protected Video(Parcel in) {
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    //Getters
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
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(type);
    }



    @Override
    public String toString() {
        return "Video{" +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
