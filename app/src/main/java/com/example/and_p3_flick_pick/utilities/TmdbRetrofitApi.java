package com.example.and_p3_flick_pick.utilities;

import com.example.and_p3_flick_pick.model.Movie;
import com.example.and_p3_flick_pick.model.MovieWrapper;
import com.example.and_p3_flick_pick.model.Review;
import com.example.and_p3_flick_pick.model.ReviewWrapper;
import com.example.and_p3_flick_pick.model.Video;
import com.example.and_p3_flick_pick.model.VideosWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbRetrofitApi {

    @GET("movie/{selection}")
    Call<MovieWrapper> getMovies(@Path("selection") String selection, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideosWrapper> getVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewWrapper> getReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

}
