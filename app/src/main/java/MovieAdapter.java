import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and_p2_popularmovies_1.R;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private ArrayList<Movie> movieArrayList;

    //Constructor for creating a MovieAdapter
    public MovieAdapter() {
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
        Movie currentMovie = movieArrayList.get(position);
// Need to set poster image...picasso?
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //Locates and stores the necessary views for each Movie list item
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPosterView;

        //Constructor for creating MovieAdapterViewHolder
        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterView = (ImageView) itemView.findViewById(R.id.list_item_iv);
        }
    }
}
