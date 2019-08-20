package com.example.and_p2_popularmovies_1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mMovieArrayList;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String SIZE = "w185";


    //Constructor for creating a com.example.and_p2_popularmovies_1.MovieAdapter
    public MovieAdapter(ArrayList<Movie> tempList) {
        mMovieArrayList = tempList;
    }

    //Override the onCreateViewHolder to inflate the list item layout
    //@return a new MovieAdapterViewHolder that holds the View for each list item
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int list_item_layout = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewToInflate = inflater.inflate(list_item_layout, parent, false);
        MovieAdapterViewHolder movieAdapterViewHolder = new MovieAdapterViewHolder(viewToInflate);
        return movieAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder viewHolder, int position) {
        Movie currentMovie = mMovieArrayList.get(position);
        //get the posterPath from the current com.example.and_p2_popularmovies_1.Movie object
        String posterPath = currentMovie.getPosterPath();
        //Generate the URL for the current com.example.and_p2_popularmovies_1.Movie object
        String posterPathString = BuildMovieURL(posterPath);
        //Include placeholder in case there is no poster path
        Picasso.get()
                .load(posterPathString)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.mPosterView);
        }


    @Override
    public int getItemCount() {
        if (null == mMovieArrayList){
            return 0;
        }else {
            return mMovieArrayList.size();
        }
    }

    //MovieAdapterViewHolder locates and stores the necessary views for each com.example.and_p2_popularmovies_1.Movie list item
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterView;

        //Constructor for creating MovieAdapterViewHolder
        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterView = (ImageView) itemView.findViewById(R.id.list_item_iv);
        }
    }

    // Helper method for generating a image URL path to pass to Picasso
    private String BuildMovieURL(String posterPath) {
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(SIZE)
                .append(posterPath);
        String fullImageString = builder.toString();
        return fullImageString;
    }

    //Helper method for updating the adapter with new movie data
    public void refreshMovieData(ArrayList<Movie> movieData){
        movieData = mMovieArrayList;
        notifyDataSetChanged();
    }


}
