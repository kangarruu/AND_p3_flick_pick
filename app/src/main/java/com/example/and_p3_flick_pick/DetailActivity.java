package com.example.and_p3_flick_pick;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.and_p3_flick_pick.databinding.ActivityDetailBinding;
import com.example.and_p3_flick_pick.model.Movie;
import com.squareup.picasso.Picasso;

import ViewModels.DetailedViewModel;
import ViewModels.DetailedViewModelFactory;
import database.MovieDatabase;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    ActivityDetailBinding mBinding;

    //Static key for receiving intent extra
    public static final String MOVIE_PARCEL = "parcel_key";
    public static final String FAVORITE_STATE_KEY = "favorite_state";

    private ActionBar actionBar;

//    private TextView titleTv;
//    private TextView overviewTv;
//    private TextView ratingTv;
//    private TextView releaseDateTv;
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

        actionBar = getSupportActionBar();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        //Initialize the database instance
        mDb = MovieDatabase.getInstance(getApplicationContext());

//        titleTv = (TextView) findViewById(R.id.title_tv);
//        overviewTv = (TextView) findViewById(R.id.overView_tv);
//        ratingTv = (TextView) findViewById(R.id.rating_tv);
//        releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        posterIv = (ImageView) findViewById(R.id.poster_iv);
        favoritesButton = (ToggleButton) findViewById(R.id.favorites_button);


        //get intent and extras from MainActivity
        Intent getClickedMovieFromMain = getIntent();
        if (getClickedMovieFromMain.hasExtra(MOVIE_PARCEL)) {
            mClickedMovie = getClickedMovieFromMain.getParcelableExtra(MOVIE_PARCEL);
        }

        if (mClickedMovie != null) {
            actionBar.setTitle(mClickedMovie.getTitle());
            mBinding.overViewTv.setText(mClickedMovie.getOverview());
            mBinding.ratingTv.setText(String.valueOf(mClickedMovie.getRating()));
            mBinding.releaseDateTv.setText(mClickedMovie.getReleaseDate().subSequence(0, 4));
            mBinding.reviewLayout.reviewContentsTv.setText(getResources().getString(R.string.review_layout_review_text_tools));
            mBinding.videoLayout.videoContentsTv.setText("LINK TO VIDEO TRAILER TESTTESTTEST");

            //set the poster image
            String posterPath = mClickedMovie.getPosterPath();
            //Generate the URL for the current Movie object
            String posterPathString = MovieAdapter.BuildMovieURL(posterPath, SIZE);
            //Include placeholder in case there is no poster path
            Picasso.get()
                    .load(posterPathString)
                    .placeholder(R.drawable.placeholder)          //Photo by Brian Kraus on Unsplash
                    .into(posterIv);

            //get the Movie id from the passed in Movie object
            final int currentMovieId = mClickedMovie.getMovieId();
            //Retrieve its favorite status from the database
            try {
                Log.d(LOG_TAG, "Checking favorite status of movie in ViewModel via LiveData");
                DetailedViewModelFactory viewModelFactory = new DetailedViewModelFactory(mDb, currentMovieId);
                final DetailedViewModel viewModel =
                        ViewModelProviders.of(this, viewModelFactory).get(DetailedViewModel.class);
                viewModel.getMovie().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(Movie movie) {
                        viewModel.getMovie().removeObserver(this);
                        if (movie != null) {
                            Log.d(LOG_TAG, "Movie is in the database, setting favorites button to checked");
                            //if movie is in the db, set the movie member variable favorite to true
                            // and set the ToggleButton as checked
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

        } else {
            Log.d(LOG_TAG, "Movie object passed in intent is null");
        }
    }

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
                //If the movie has been unchecked, delete the movie from the database
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
