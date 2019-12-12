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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.and_p3_flick_pick.model.Movie;
import com.example.and_p3_flick_pick.model.MovieWrapper;
import com.example.and_p3_flick_pick.utilities.NetworkUtils;
import com.example.and_p3_flick_pick.utilities.TmdbRetrofitApi;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ViewModels.MainViewModel;
import database.MovieDatabase;
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

    private static List<Movie> movieList;
    private static List<Movie> favoritesList;

    private URL sortUrl = null;

    private final static int SPAN_COUNT_PORT = 2;
    private final static int SPAN_COUNT_LAND = 4;

    //constant for restoring sort selection for onInstanceState
    private static final String MOVIELIST_INSTANCE_KEY = "save_state";
    private static final String TOOLBAR_INSTANCE_KEY = "toolbar_state";

    //Constants for sort option endpoints
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String QUERY_BASE_POPULAR = BASE_URL + "popular?api_key=";
    private static final String QUERY_BASE_RATING = BASE_URL + "top_rated?api_key=";
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

        //Save the movieList to the savedInstanceState bundle for retrieval
//        if (savedInstanceState != null) {
//            Log.d(LOG_TAG, "Getting savedInstanceState");
//            actionBar.setTitle(savedInstanceState.getCharSequence(TOOLBAR_INSTANCE_KEY));
//            movieList= savedInstanceState.getParcelableArrayList(MOVIELIST_INSTANCE_KEY);
//            mMovieAdapter.refreshMovieData(movieList);
//        } else {
//            Log.d(LOG_TAG, "No instanceState Saved, run API query");
//            sortUrl = NetworkUtils.buildUrl(QUERY_BASE_POPULAR);
//            // Check for internet connection and
//            // Execute an AsyncTask for making http requests
//            if (isNetworkConnected()){
//                new TmdbQueryAsyncTask().execute(sortUrl);
//            } else {
//                Log.d(LOG_TAG, "Unable to execute TmdbQueryAsyncTask() due to internet connectivity issues ");
//                showErrorMessage();
//            }
//        }

//        //Set up ViewModel and set an observer on the favorite's list
//        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(List<Movie> movies) {
//                Log.d(LOG_TAG, "Receiving updated favorites list from LiveData in ViewModel");
//                //assign favoritesList to the updated favorite's list to be set on adapter after settings click
//                favoritesList = movies;
//            }
//        });


        //RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbRetrofitApi tmdbRetrofitApi = retrofit.create(TmdbRetrofitApi.class);
        Call<MovieWrapper> movieRetrofitCall = tmdbRetrofitApi.getPopularMovies(API_KEY);
        Log.d(LOG_TAG, "Making retrofitAPI call.");
        movieRetrofitCall.enqueue(new Callback<MovieWrapper>() {
            @Override
            public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                if(!response.isSuccessful()){
                    Log.d(LOG_TAG, "http request was unsuccessful");
                    showErrorMessage();
                    return;
                }

                movieList = response.body().getMovies();
                Log.d(LOG_TAG, movieList.toString());

                for (Movie movie :  movieList) {
                    String posterPath = movie.getPosterPath();
                    String backdropPath = movie.getBackdropPath();
                    String title = movie.getTitle();
                    double rating = movie.getRating();
                    String overview = movie.getOverview();
                    String releaseDate = movie.getReleaseDate();

                    Log.d(LOG_TAG, "Attaching movie to parsedMovieList" + movieList);

                }

                Log.d(LOG_TAG, "Attaching Retrofit movie list to adapter");
                mMovieAdapter.refreshMovieData(movieList);

            }

            @Override
            public void onFailure(Call<MovieWrapper> call, Throwable t) {
                Log.d(LOG_TAG, "Http call failed" + t);
                showErrorMessage();
            }
        });

    }


    //Helper code for checking for internet connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
//                sortUrl = NetworkUtils.buildUrl(QUERY_BASE_POPULAR);
//                new TmdbQueryAsyncTask().execute(sortUrl);
                actionBar.setTitle(R.string.toolbar_title_popular_movies);
                break;
            }
            case R.id.menu_sort_rated: {
//                sortUrl = NetworkUtils.buildUrl(QUERY_BASE_RATING);
//                new TmdbQueryAsyncTask().execute(sortUrl);
                actionBar.setTitle(R.string.toolbar_title_high_rated_movies);
                break;
            }
            case R.id.menu_display_favorites: {
//                loadFavoritesFromViewModel();
                actionBar.setTitle(R.string.toolbar_title_my_favorites);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

//    private void loadFavoritesFromViewModel()  {
//        //assign the updated favoritesList to movieList for saving in onInstanceState
//        movieList = (ArrayList) favoritesList;
//        //Load updated favorites list saved in the viewModel
//        mMovieAdapter.refreshMovieData(movieList);
//    }


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

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        CharSequence toolbarTitleState = actionBar.getTitle();
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(MOVIELIST_INSTANCE_KEY, movieList);
//        outState.putCharSequence(TOOLBAR_INSTANCE_KEY, toolbarTitleState);
//    }

//    private class TmdbQueryAsyncTask extends AsyncTask<URL, Void, String>{
//
//        //Display the loading indicator while loading Movie data
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingPb.setVisibility(View.VISIBLE);
//        }
//
//        //Run the http request off the main thread
//        //@param an Array of urls
//        //@return a String with the contents of the http request
//        @Override
//        protected String doInBackground(URL... urls) {
//
//            String movieJsonResult = null;
//            //Don't perform the request if there are no URLs or the first is null
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            URL movieListQueryUrl = urls[0];
//            try {
//                movieJsonResult = NetworkUtils.getHttpResponse(movieListQueryUrl);
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Unable to call getHttpResponse() from MainActivity AsyncTask", e);
//            }
//            return movieJsonResult;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            //As soon as AsyncTask is finished, hide the loading indicator
//            mLoadingPb.setVisibility(View.INVISIBLE);
//            if (s != null && !s.isEmpty()){
//                //Parse the response String
//                // @return an ArrayList of Movie objects to update the adapter with
//                movieList = NetworkUtils.parseJsonResponse(s);
//                mMovieAdapter.refreshMovieData(movieList);
//                //Hide the error message
//                showMovieData();
//            }else {
//                showErrorMessage();
//            }
//        }
//    }
//

}
