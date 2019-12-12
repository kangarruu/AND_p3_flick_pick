package com.example.and_p3_flick_pick.utilities;

import com.example.and_p3_flick_pick.model.Movie;
import com.example.and_p3_flick_pick.model.MovieWrapper;
import com.example.and_p3_flick_pick.model.Review;
import com.example.and_p3_flick_pick.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbRetrofitApi {

    @GET("popular")
    Call<MovieWrapper> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MovieWrapper> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("/movie/{movie_id}/videos")
    Call<List<Video>> getVideos(@Path("movie_id") int movieId);

    @GET("/movie/{movie_id}/reviews")
    Call<List<Review>> getReviews(@Path("movie_id") int movieId);


}
