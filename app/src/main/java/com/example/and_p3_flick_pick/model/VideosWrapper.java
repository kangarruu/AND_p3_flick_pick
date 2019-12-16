package com.example.and_p3_flick_pick.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosWrapper {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<Video> videos = null;

    public int getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setResults(List<Video> videos) {
        this.videos = videos;
    }

}