package com.example.and_p3_flick_pick;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and_p3_flick_pick.model.Review;
import com.example.and_p3_flick_pick.model.ReviewWrapper;
import com.example.and_p3_flick_pick.utilities.TmdbRetrofitApi;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private Context mContext;
    private static List<Review> mReviewList;
    private static TmdbRetrofitApi tmdbRetrofitApi;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";


    //Constructor for creating a ReviewAdapter
    public ReviewAdapter(Context context, List<Review> tempList) {
        mContext = context;
        mReviewList = tempList;
    }

    //Override the onCreateViewHolder to inflate the list item layout
    //@return a new ReviewAdapterViewHolder that holds the View for each list item
    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int list_item_layout = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewToInflate = inflater.inflate(list_item_layout, parent, false);
        return new ReviewAdapterViewHolder(viewToInflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder viewHolder, int position) {
        Review currentReview = mReviewList.get(position);

        String currentAuthor = currentReview.getAuthor();
        viewHolder.authorTv.setText(currentAuthor);

        String reviewContents = currentReview.getContent();
        viewHolder.reviewContentsTv.setText(reviewContents);

        String readMoreUrl = currentReview.getUrl();
        viewHolder.readMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchTmdbReviews = new Intent(Intent.ACTION_VIEW, Uri.parse(readMoreUrl));
                mContext.startActivity(launchTmdbReviews);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == mReviewList){
            return 0;
        }else {
            return mReviewList.size();
        }
    }

    //ReviewAdapterViewHolder locates and stores the necessary views for each Review list item
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorTv;
        private final TextView reviewContentsTv;
        private final MaterialButton readMoreBtn;

        //Constructor for creating ReviewAdapterViewHolder
        private ReviewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.review_author_tv);
            reviewContentsTv = itemView.findViewById(R.id.review_contents_tv);
            readMoreBtn = itemView.findViewById(R.id.review_read_btn);
        }

    }

    //Helper method for updating the adapter with new Review data
    public void refreshReviewData(List<Review> ReviewData){
        mReviewList = ReviewData ;
        notifyDataSetChanged();
    }

}
