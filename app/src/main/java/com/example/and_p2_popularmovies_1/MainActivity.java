package com.example.and_p2_popularmovies_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_p2_popularmovies_1.model.Movie;
import com.example.and_p2_popularmovies_1.utilities.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickHandler {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Static key for intent extra
    private static final String MOVIE_PARCEL = "parcel_key";

    private ProgressBar mLoadingPb;
    private TextView mErrorTv;
    private RecyclerView mMoviesRv;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieList;

    //for debugging
    private URL tempUrl = null;


    private final static int SPAN_COUNT = 2;



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

        try {
            tempUrl = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=babc627746a594b8781282dd36606cd8");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Execute an AsyncTask for making http requests
        new TmdbQueryAsyncTask().execute(tempUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //helper methods to show/hide the loading indicator and error textView
    private void showErrorMessage() {
        mErrorTv.setVisibility(View.VISIBLE);
        mLoadingPb.setVisibility(View.INVISIBLE);
    }

    private void showLoadingIndicator(){
        mErrorTv.setVisibility(View.INVISIBLE);
        mLoadingPb.setVisibility(View.VISIBLE);
    }

    //Launch detail activity upon click
    @Override
    public void onListItemClick(Movie clickedMovie) {
        Intent startDetailActivity = new Intent(this, DetailActivity.class);
        startDetailActivity.putExtra(MOVIE_PARCEL, clickedMovie);
        startActivity(startDetailActivity);
    }

    public class TmdbQueryAsyncTask extends AsyncTask<URL, Void, String>{

        //Run the http request off the main thread
        //@param an Array of urls
        //@return a String with the contents of the http request
        @Override
        protected String doInBackground(URL... urls) {
            String queryResults = null;
            //Dont perform the request if there are no URLs or the first is null
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
            }else {
                showErrorMessage();
            }
        }
    }
}
