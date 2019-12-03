package com.example.and_p3_flick_pick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.and_p3_flick_pick.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    //Static key for receiving intent extra
    public static final String MOVIE_PARCEL = "parcel_key";
    private TextView titleTv;
    private TextView overviewTv;
    private TextView ratingTv;
    private TextView releaseDateTv;
    private ImageView posterIv;

    private Movie mClickedMovie;
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

        titleTv = (TextView) findViewById(R.id.title_tv);
        overviewTv = (TextView) findViewById(R.id.overView_tv);
        ratingTv = (TextView) findViewById(R.id.rating_tv);
        releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        posterIv = (ImageView) findViewById(R.id.poster_iv);

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

}
