package com.example.and_p3_flick_pick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and_p3_flick_pick.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovieList;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String SIZE = "w342";
    private final MovieAdapterClickHandler mClickListener;


    //Constructor for creating a MovieAdapter
    public MovieAdapter(List<Movie> tempList, MovieAdapterClickHandler clickHandler) {
        mMovieList = tempList;
        mClickListener = clickHandler;
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
        return new MovieAdapterViewHolder(viewToInflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder viewHolder, int position) {
        Movie currentMovie = mMovieList.get(position);
        //get the posterPath from the current Movie object
        String posterPath = currentMovie.getPosterPath();
        //Generate the URL for the current Movie object
        String posterPathString = BuildMovieURL(posterPath, SIZE);
        //Include placeholder in case there is no poster path
        Picasso.get()
                .load(posterPathString)
                .placeholder(R.drawable.placeholder)            //Photo by Brian Kraus on Unsplash
                .into(viewHolder.mPosterView);
        }


    @Override
    public int getItemCount() {
        if (null == mMovieList){
            return 0;
        }else {
            return mMovieList.size();
        }
    }

    public interface MovieAdapterClickHandler {
        void onListItemClick(Movie clickedMovie);
    }

    //MovieAdapterViewHolder locates and stores the necessary views for each Movie list item
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mPosterView;

        //Constructor for creating MovieAdapterViewHolder
        private MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterView = (ImageView) itemView.findViewById(R.id.list_item_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            Movie currentMovie = mMovieList.get(itemPosition);
            mClickListener.onListItemClick(currentMovie);
        }
    }

    // Helper method for generating a image URL path to pass to Picasso
    public static String BuildMovieURL(String posterPath, String size) {
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(size)
                .append(posterPath);
        return builder.toString();
    }

    //Helper method for updating the adapter with new movie data
    public void refreshMovieData(List<Movie> movieData){
        mMovieList = movieData ;
        notifyDataSetChanged();
    }

}
