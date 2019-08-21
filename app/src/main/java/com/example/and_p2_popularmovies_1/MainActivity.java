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

    private ProgressBar mLoadingPb;
    private TextView mErrorTv;
    private RecyclerView mMoviesRv;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieList;

    private final static int SPAN_COUNT = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorTv = (TextView) findViewById(R.id.main_error_msg_tv);
        mLoadingPb = (ProgressBar) findViewById(R.id.main_pb);

        mMoviesRv = (RecyclerView) findViewById(R.id.main_rv);

//      Debugging mock data. Delete after http call
//        final ArrayList<Movie> debugList = new ArrayList<>();
//        debugList.add(new Movie("Moonlight","/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg","Grace VanderWaal",7.2,"2019-07-19" ));
//        debugList.add(new Movie("Sick of Being Told", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2,"2019-07-19" ));
//        debugList.add(new Movie("Burned", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("Just a Crush" ));
//        debugList.add(new Movie("So Much More Than This", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("Escape My Mind", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("Talk Good", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("Florets", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("Insane Sometimes", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("A Better Life", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("City Song", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
//        debugList.add(new Movie("Darkness Keeps Chasing Me", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal", 7.2, "2019-07-19"));

        mMovieAdapter = new MovieAdapter(movieList, this);
        mMoviesRv.setAdapter(mMovieAdapter);
        //Create a new GridLayoutManager to display the movie posters
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMoviesRv.setLayoutManager(layoutManager);
        mMoviesRv.hasFixedSize();

        URL tempUrl = null;
        try {
            tempUrl = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=babc627746a594b8781282dd36606cd8");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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
        startDetailActivity.putExtra(Intent.EXTRA_PACKAGE_NAME, clickedMovie);
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
