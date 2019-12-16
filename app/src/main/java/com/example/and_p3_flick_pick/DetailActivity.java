package com.example.and_p3_flick_pick;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.and_p3_flick_pick.databinding.ActivityDetailBinding;
import com.example.and_p3_flick_pick.model.Movie;
import com.example.and_p3_flick_pick.model.Review;
import com.example.and_p3_flick_pick.model.ReviewWrapper;
import com.example.and_p3_flick_pick.model.Video;
import com.example.and_p3_flick_pick.model.VideosWrapper;
import com.example.and_p3_flick_pick.utilities.TmdbRetrofitApi;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import ViewModels.DetailedViewModel;
import ViewModels.DetailedViewModelFactory;
import database.MovieDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private ActionBar actionBar;

    //Constant for image size for Picasso API call
    private static final String SIZE = "w500";

    //Togglebutton for inserting favorites into the db
    private ToggleButton favoritesButton;

    private ActivityDetailBinding mBinding;
    private ReviewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;


    //Retrofit API instance and related member variables/constants
    private TmdbRetrofitApi tmdbRetrofitApi;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String YOUTUBE_APP = "vnd.youtube:";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private int currentMovieId;
    private String youTubeKey;
    private List<Video> videosList;
    private static List<Review> reviewsList = null;

    //Constant for retrieving intent extra
    public static final String MOVIE_PARCEL = "parcel_key";
    //Member variable for Movie object retrieved from Intent
    private Movie mClickedMovie;

    //Member variable for the Database;
    private MovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Set up dataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        //initialize a ReviewAdapter and set it on the RecyclerView
        mAdapter = new ReviewAdapter(DetailActivity.this, reviewsList);
        mBinding.reviewLayout.reviewRv.setAdapter(mAdapter);
        linearLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mBinding.reviewLayout.reviewRv.setLayoutManager(linearLayoutManager);

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detailed_activity_title);


        favoritesButton = (ToggleButton) findViewById(R.id.favorites_button);

        mDb = MovieDatabase.getInstance(getApplicationContext());

        //Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdbRetrofitApi = retrofit.create(TmdbRetrofitApi.class);

        //get intent and extras from MainActivity
        Intent getClickedMovieFromMain = getIntent();
        if (getClickedMovieFromMain.hasExtra(MOVIE_PARCEL)) {
            mClickedMovie = getClickedMovieFromMain.getParcelableExtra(MOVIE_PARCEL);
        }

        //Bind the movie object to the UI
        if (mClickedMovie != null) {
            mBinding.imageLayout.titleTv.setText(mClickedMovie.getTitle());
            mBinding.overViewTv.setText(mClickedMovie.getOverview());
            mBinding.ratingTv.setText(String.valueOf(mClickedMovie.getRating()));
            mBinding.releaseDateTv.setText(mClickedMovie.getReleaseDate().subSequence(0, 4));

            //set the movie image
            String backdropPath = mClickedMovie.getBackdropPath();
            //Generate the URL for the current Movie object
            String backdropPathString = MovieAdapter.BuildMovieURL(backdropPath, SIZE);
            //Include placeholder in case there is no poster path
            Picasso.get()
                    .load(backdropPathString)
                    .placeholder(R.drawable.placeholder)          //Photo by Brian Kraus on Unsplash
                    .into(mBinding.imageLayout.backdropIv);

            //get the Movie id from the passed in Movie object
            currentMovieId = mClickedMovie.getMovieId();
            //Retrieve its favorite status from the database and set it on the ToggleButton
            setFavoriteStatus(currentMovieId);

            //Use the id to make Retrofit Api call for videos and populate the UI
            getVideosWithRetrofit(currentMovieId);
            //Set a clickListener on the playbutton
            mBinding.videoLayout.videoPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchYoutubeVideo(youTubeKey);
                }
            });

            //Use the id to make Retrofit Api call for reviews and populate the UI with the adapter
            try {
                getReviewsWithRetrofit(currentMovieId);
            } catch (NullPointerException e){
                Log.d(LOG_TAG, "Attempted to get reviews with Retrofit, but no reviews available");
            }

        } else {
            Log.d(LOG_TAG, "Movie object passed in intent is null");
        }
    }

    //Make TMDB videos API HTTP request though Retrofit
    private void getVideosWithRetrofit(int movieId) {
        Call<VideosWrapper> videosRetrofitCall = tmdbRetrofitApi.getVideos(currentMovieId, API_KEY);
        Log.d(LOG_TAG, "Retrofit requesting videos from TMDB.");
        videosRetrofitCall.enqueue(new Callback<VideosWrapper>() {

            @Override
            public void onResponse(Call<VideosWrapper> call, Response<VideosWrapper> response) {

                if(!response.isSuccessful()){
                    Log.d(LOG_TAG, "Video request was unsuccessful");
                    showVideoErrorMessage();
                    return;
                }

                videosList = response.body().getVideos();
                //Get the key from the first video and save it in the youTubeKey member variable
                Video firstVideo = videosList.get(0);
                youTubeKey = firstVideo.getKey();
            }

            @Override
            public void onFailure(Call<VideosWrapper> call, Throwable t) {
                Log.d(LOG_TAG, "Retrofit video Api request failed" + t);
                showVideoErrorMessage();
            }
        });

    }

    public void launchYoutubeVideo(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + key));

        try{
            startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }

    private void showVideoErrorMessage() {
        mBinding.videoLayout.videoPlayButton.setVisibility(View.INVISIBLE);
        mBinding.videoLayout.videoErrorTv.setVisibility(View.VISIBLE);
        mBinding.videoLayout.videoErrorTv.setText(getResources().getString(R.string.detail_videos_error_msg));
    }

//    public void showReviewErrorMessage() {
//        mBinding.reviewLayout.reviewRv.setVisibility(View.GONE);
//        mBinding.reviewLayout.reviewErrorTv.setVisibility(View.VISIBLE);
//        mBinding.reviewLayout.reviewErrorTv.setText(getResources().getString(R.string.detail_reviews_error_msg));
//    }


    //Make TMDB reviews API HTTP request though Retrofit
    private void getReviewsWithRetrofit(int movieId) {
        Call<ReviewWrapper> reviewRetrofitCall = tmdbRetrofitApi.getReviews(movieId, API_KEY);
        Log.d(LOG_TAG, "Retrofit requesting reviews from TMDB.");
        reviewRetrofitCall.enqueue(new Callback<ReviewWrapper>() {
            @Override
            public void onResponse(Call<ReviewWrapper> call, Response<ReviewWrapper> response) {
                if(!response.isSuccessful()){
                    Log.d(LOG_TAG, "Review request was unsuccessful");
                    return;
                }

                reviewsList = response.body().getReviews();
                mAdapter.refreshReviewData(reviewsList);

            }

            @Override
            public void onFailure(Call<ReviewWrapper> call, Throwable t) {
                Log.d(LOG_TAG, "Retrofit video Api request failed" + t);
            }
        });

    }

    //Method for retrieving the favorite status of a movie stored in Room and setting the toggleButton
    //@param the clicked movie id
    private void setFavoriteStatus(int id) {
            final int movieId = id;
            try {
                Log.d(LOG_TAG, "Checking favorite status of movie in ViewModel via LiveData");
                DetailedViewModelFactory viewModelFactory = new DetailedViewModelFactory(mDb, movieId);
                final DetailedViewModel viewModel =
                        ViewModelProviders.of(this, viewModelFactory).get(DetailedViewModel.class);
                viewModel.getMovie().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(Movie movie) {
                        viewModel.getMovie().removeObserver(this);
                        if (movie != null) {
                            //if movie is in the db, set favorite to true in mClickedMovie
                            // and set the ToggleButton as checked
                            Log.d(LOG_TAG, "Movie is in the database, setting favorites button to checked");
                            mClickedMovie.setFavorite(true);
                            favoritesButton.setChecked(true);
                        }else{
                            Log.d(LOG_TAG, "Movie is not in the database, leave favorite button unchecked");
                        }
                    }
                });
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "NullPointerException when checking clicked movie's id.");
            }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //Set the change listener on the favorites button
        favoritesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //If the ToggleButton is selected and it is not already a favorite, add it to the database
                if(isChecked) {
                    if (!mClickedMovie.getFavorite()) {
                        addMovieToFavorites();
                    } else {
                        Log.d(LOG_TAG, "Movie already in favorites, no need to insert to database");
                    }
                //If toggleButton has been unchecked, delete the movie from the database
                } else {
                    deleteMovieFromFavorites();
                }
            }
        });
    }

    public void addMovieToFavorites() {
        //Set this movie object favorite boolean to true and add it to the db
        mClickedMovie.setFavorite(true);
        //Run db insert operation off the main thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(mClickedMovie);
                Log.d(LOG_TAG, "Movie inserted into the db");
            }
        });

    }

    public void deleteMovieFromFavorites() {
        //Set this movie object favorite boolean to false and remove it from the db
        mClickedMovie.setFavorite(false);
        //Run db delete operation off the main thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(mClickedMovie);
                Log.d(LOG_TAG, "Movie deleted from db");
            }
        });
    }

}
