package com.example.and_p3_flick_pick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.and_p3_flick_pick.model.Movie;
import com.squareup.picasso.Picasso;

import database.MovieDatabase;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    //Static key for receiving intent extra
    public static final String MOVIE_PARCEL = "parcel_key";
    public static final String FAVORITE_STATE_KEY = "favorite_state";

    private TextView titleTv;
    private TextView overviewTv;
    private TextView ratingTv;
    private TextView releaseDateTv;
    private ImageView posterIv;
    private ToggleButton favoritesButton;

    //Member variable for Movie object retrieved from Intent
    private Movie mClickedMovie;

    //Member variable for the Database;
    private MovieDatabase mDb;

    //Constant for image size retrieved during API call
    private static final String SIZE = "w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    //get intent and extras from MainActivity
        Intent getClickedMovieFromMain = getIntent();
        if (getClickedMovieFromMain.hasExtra(MOVIE_PARCEL)){
            mClickedMovie = getClickedMovieFromMain.getParcelableExtra(MOVIE_PARCEL);
        }

        //Initialize the database instance
        mDb = MovieDatabase.getInstance(getApplicationContext());

        titleTv = (TextView) findViewById(R.id.title_tv);
        overviewTv = (TextView) findViewById(R.id.overView_tv);
        ratingTv = (TextView) findViewById(R.id.rating_tv);
        releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        posterIv = (ImageView) findViewById(R.id.poster_iv);
        favoritesButton = (ToggleButton) findViewById(R.id.favoritesButton);

        if (mClickedMovie != null){
            titleTv.setText(mClickedMovie.getTitle());
            overviewTv.setText(mClickedMovie.getOverview());
            ratingTv.setText(String.valueOf(mClickedMovie.getRating()));
            releaseDateTv.setText(mClickedMovie.getReleaseDate().subSequence(0,4));

            //set the poster image
            String posterPath = mClickedMovie.getPosterPath();
            //Generate the URL for the current Movie object
            String posterPathString = MovieAdapter.BuildMovieURL(posterPath, SIZE);
            //Include placeholder in case there is no poster path
            Picasso.get()
                    .load(posterPathString)
                    .placeholder(R.drawable.placeholder)            //Photo by Brian Kraus on Unsplash
                    .into(posterIv);
        } else {
            Log.d(LOG_TAG, "mClickedMovie is null");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Retrieve favorite status from the database by the Movie's id
        int currentMovieId = mClickedMovie.getMovieId();
        try {
            Movie thisMovie = mDb.movieDao().loadMovieById(currentMovieId);
            Log.d(LOG_TAG, "Retrieving favorite status. thisMovie.getFavorite() is: "  + thisMovie.getFavorite());
            if (thisMovie != null){
                if (thisMovie.getFavorite()) {
                    favoritesButton.setChecked(true);
                } else {
                    favoritesButton.setChecked(false);
                }
            }
        } catch (NullPointerException e){
            Log.e(LOG_TAG, "thisMovie is not in the database" );
        }

        //Set the change listener on the favorites button
        favoritesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addMovieToFavorites();
                } else {
                    deleteMovieFromFavorites();
                }
            }
        });
    }

    public void addMovieToFavorites() {
        mClickedMovie.setFavorite(true);
        mDb.movieDao().insertMovie(mClickedMovie);
        Log.d(LOG_TAG, "Movie inserted into the db:" + mDb.movieDao().loadAllMovies());
    }

    public void deleteMovieFromFavorites() {
        mClickedMovie.setFavorite(false);
        mDb.movieDao().deleteMovie(mClickedMovie);
        Log.d(LOG_TAG, "Movie deleted from db:" + mDb.movieDao().loadAllMovies());
    }

}
