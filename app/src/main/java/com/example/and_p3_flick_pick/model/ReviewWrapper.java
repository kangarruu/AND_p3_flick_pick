package com.example.and_p3_flick_pick.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewWrapper {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<Review> reviews = null;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("total_results")
    @Expose
    private int totalResults;


    //Getters
    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

}
