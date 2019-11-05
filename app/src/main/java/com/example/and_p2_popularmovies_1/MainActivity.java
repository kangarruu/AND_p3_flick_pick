package com.example.and_p2_popularmovies_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.and_p2_popularmovies_1.model.Movie;
import com.example.and_p2_popularmovies_1.utilities.NetworkUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickHandler {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ProgressBar mLoadingPb;
    private TextView mErrorTv;
    private RecyclerView mMoviesRv;
    private static MovieAdapter mMovieAdapter;

    private static ArrayList<Movie> movieList;

    private URL sortUrl = null;

    private final static int SPAN_COUNT = 2;

    //Constants for sort option endpoints
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String QUERY_BASE_POPULAR = BASE_URL + "popular?api_key=";
    private static final String QUERY_BASE_RATING = BASE_URL + "top_rated?api_key=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorTv = (TextView) findViewById(R.id.main_error_msg_tv);
        mLoadingPb = (ProgressBar) findViewById(R.id.main_pb);
        mMoviesRv = (RecyclerView) findViewById(R.id.main_rv);

        //initialize a MovieAdapter and set it on the RecyclerView
        mMovieAdapter = new MovieAdapter(movieList, this);
        mMoviesRv.setAdapter(mMovieAdapter);

        //Create a new GridLayoutManager to display the movie posters
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMoviesRv.setLayoutManager(layoutManager);
        mMoviesRv.hasFixedSize();

        sortUrl = NetworkUtils.buildUrl(QUERY_BASE_POPULAR);

        // Check for internet connection and
        // Execute an AsyncTask for making http requests
        if (isNetworkConnected()){
            new TmdbQueryAsyncTask().execute(sortUrl);
        } else {
            Log.d(LOG_TAG, "Unable to execute TmdbQueryAsyncTask() due to internet connectivity issues ");
            showErrorMessage();
        }

    }

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
                sortUrl = NetworkUtils.buildUrl(QUERY_BASE_POPULAR);
                break;
            }
            case R.id.menu_sort_rated: {
                sortUrl = NetworkUtils.buildUrl(QUERY_BASE_RATING);
                break;
            }
        }
        new TmdbQueryAsyncTask().execute(sortUrl);
        return super.onOptionsItemSelected(item);
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

    private class TmdbQueryAsyncTask extends AsyncTask<URL, Void, String>{

        //Display the loading indicator while loading Movie data
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPb.setVisibility(View.VISIBLE);
        }

        //Run the http request off the main thread
        //@param an Array of urls
        //@return a String with the contents of the http request
        @Override
        protected String doInBackground(URL... urls) {

            String queryResults = null;
            //Don't perform the request if there are no URLs or the first is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            URL queryUrl = urls[0];
            try {
                queryResults = NetworkUtils.getHttpResponse(queryUrl);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Unable to call getHttpResponse()", e);
            }
            return queryResults;
        }

        @Override
        protected void onPostExecute(String s) {
            //As soon as AsyncTask is finished, hide the loading indicator
            mLoadingPb.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()){
                //Parse the response String
                // @return an ArrayList of Movie objects to update the adapter with
                movieList = NetworkUtils.parseJsonResponse(s);
                mMovieAdapter.refreshMovieData(movieList);
                //Hide the error message
                showMovieData();
            }else {
                showErrorMessage();
            }
        }
    }

}
