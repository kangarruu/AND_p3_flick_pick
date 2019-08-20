package com.example.and_p2_popularmovies_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.and_p2_popularmovies_1.model.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
        final ArrayList<Movie> debugList = new ArrayList<>();
        debugList.add(new Movie("Moonlight","/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg","Grace VanderWaal",7.2,"2019-07-19" ));
        debugList.add(new Movie("Sick of Being Told", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2,"2019-07-19" ));
        debugList.add(new Movie("Burned", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("Just a Crush" ));
        debugList.add(new Movie("So Much More Than This", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("Escape My Mind", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("Talk Good", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("Florets", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("Insane Sometimes", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("A Better Life", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("City Song", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal",7.2, "2019-07-19" ));
        debugList.add(new Movie("Darkness Keeps Chasing Me", "/a4BfxRK8dBgbQqbRxPs8kmLd8LG.jpg", "Grace VanderWaal", 7.2, "2019-07-19"));

        mMovieAdapter = new MovieAdapter(debugList);
        mMoviesRv.setAdapter(mMovieAdapter);
        //Create a new GridLayoutManager to display the movie posters
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mMoviesRv.setLayoutManager(layoutManager);
        mMoviesRv.hasFixedSize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
