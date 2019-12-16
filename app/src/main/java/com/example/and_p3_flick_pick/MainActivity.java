package com.example.and_p3_flick_pick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.and_p3_flick_pick.model.Movie;
import com.example.and_p3_flick_pick.model.MovieWrapper;
import com.example.and_p3_flick_pick.utilities.TmdbRetrofitApi;

import java.util.ArrayList;
import java.util.List;

import ViewModels.MainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickHandler {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ActionBar actionBar;
    private ProgressBar mLoadingPb;
    private TextView mErrorTv;
    private static RecyclerView mMoviesRv;
    private static MovieAdapter mMovieAdapter;

    //Constants for defining number of columns in the layoutManager
    private final static int SPAN_COUNT_PORT = 2;
    private final static int SPAN_COUNT_LAND = 4;

    //member variable to hold the movieList displayed by the adapter
    private static ArrayList<Movie> movieList;
    //member variable for holding viewModel's latest favorites list
    private static List<Movie> favoritesList;

    //constant for restoring toolbar and movieLIst from onInstanceState
    private static final String MOVIELIST_INSTANCE_KEY = "save_state";
    private static final String TOOLBAR_INSTANCE_KEY = "toolbar_state";

    //Retrofit API instance and Constants for base URL, API key and sort option endpoints
    private TmdbRetrofitApi tmdbRetrofitApi;
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_RATING = "top_rated";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.toolbar_title_popular_movies);

        mErrorTv = (TextView) findViewById(R.id.main_error_msg_tv);
        mLoadingPb = (ProgressBar) findViewById(R.id.main_pb);
        mMoviesRv = (RecyclerView) findViewById(R.id.main_rv);

        //initialize a MovieAdapter and set it on the RecyclerView
        mMovieAdapter = new MovieAdapter(movieList, this);
        mMoviesRv.setAdapter(mMovieAdapter);

        //Set a LayoutManager on the RecyclerView and set the number of columns based on orientation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mMoviesRv.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT_PORT));
        } else {
            mMoviesRv.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT_LAND));
        }
        mMoviesRv.hasFixedSize();

        //Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbRetrofitApi = retrofit.create(TmdbRetrofitApi.class);

        //If an instance was saved in the bundle, set the movieList on the adapter
        //and set the appropriate toolbar title
        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "Getting savedInstanceState");
            actionBar.setTitle(savedInstanceState.getCharSequence(TOOLBAR_INSTANCE_KEY));
            movieList= savedInstanceState.getParcelableArrayList(MOVIELIST_INSTANCE_KEY);
            mMovieAdapter.refreshMovieData(movieList);
        } else {
            // Check for internet connection and run retrofit request
            Log.d(LOG_TAG, "No instanceState Saved, run API query");
            if (isNetworkConnected()){
                getMoviesWithRetrofit(PATH_POPULAR);
            } else {
                Log.d(LOG_TAG, "Unable to execute Retrofit Api request due to internet connectivity issues ");
                showErrorMessage();
            }
        }

        //Set up ViewModel and set an observer on the favorite's list
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d(LOG_TAG, "Receiving updated favorites list from LiveData in ViewModel");
                //assign favoritesList to the updated favorite's list to be set on adapter after settings click
                favoritesList = movies;
            }
        });

    }

    //Helper method for checking for internet connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    //Make TMDB API HTTP request though Retrofit
    private void getMoviesWithRetrofit(String selection) {

        Call<MovieWrapper> movieRetrofitCall = tmdbRetrofitApi.getMovies(selection, API_KEY);
        Log.d(LOG_TAG, "Retrofit requesting movies from TMDB.");
        movieRetrofitCall.enqueue(new Callback<MovieWrapper>() {
            @Override
            public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                if(!response.isSuccessful()){
                    Log.d(LOG_TAG, "http request was unsuccessful");
                    showErrorMessage();
                    return;
                }

                showMovieData();
                movieList = response.body().getMovies();
                Log.d(LOG_TAG, "Attaching Retrofit movie list to adapter");
                mMovieAdapter.refreshMovieData(movieList);
            }

            @Override
            public void onFailure(Call<MovieWrapper> call, Throwable t) {
                Log.d(LOG_TAG, "Retrofit Api request failed" + t);
                showErrorMessage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_sort_popular: {
                getMoviesWithRetrofit(PATH_POPULAR);
                actionBar.setTitle(R.string.toolbar_title_popular_movies);
                break;
            }
            case R.id.menu_sort_rated: {
                getMoviesWithRetrofit(PATH_RATING);
                actionBar.setTitle(R.string.toolbar_title_high_rated_movies);
                break;
            }
            case R.id.menu_display_favorites: {
                attachFavoritesFromViewModel();
                actionBar.setTitle(R.string.toolbar_title_my_favorites);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachFavoritesFromViewModel()  {
        //assign the updated favoritesList to movieList for saving in onInstanceState
        movieList = (ArrayList) favoritesList;
        //Load updated favorites list saved in the viewModel
        Log.d(LOG_TAG, "Attaching ViewModel list to adapter. " + movieList);
        mMovieAdapter.refreshMovieData(movieList);
    }

    //helper methods to show/hide the loading indicator and error textView
    private void showErrorMessage() {
        mLoadingPb.setVisibility(View.INVISIBLE);
        mMoviesRv.setVisibility(View.INVISIBLE);
        mErrorTv.setVisibility(View.VISIBLE);
    }

    private void showMovieData(){
        mErrorTv.setVisibility(View.INVISIBLE);
        mMoviesRv.setVisibility(View.VISIBLE);
    }

    //Launch detail activity upon click
    @Override
    public void onListItemClick(Movie clickedMovie) {
        Intent startDetailActivity = new Intent(this, DetailActivity.class);
        startDetailActivity.putExtra(DetailActivity.MOVIE_PARCEL, clickedMovie);
        startActivity(startDetailActivity);
    }

    //Save the toolbar title and current state of movieList into instanceState bundle
    //for retrieval after rotation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        CharSequence toolbarTitleState = actionBar.getTitle();
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIELIST_INSTANCE_KEY, movieList);
        outState.putCharSequence(TOOLBAR_INSTANCE_KEY, toolbarTitleState);
    }

}
