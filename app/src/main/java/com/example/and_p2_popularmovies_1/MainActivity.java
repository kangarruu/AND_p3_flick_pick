package com.example.and_p2_popularmovies_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ProgressBar mLoadingPb;
    private RecyclerView mMoviesRv;
    private TextView mErrorTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorTv = (TextView) findViewById(R.id.main_error_msg_tv);
        mMoviesRv = (RecyclerView) findViewById(R.id.main_rv);
        mLoadingPb = (ProgressBar) findViewById(R.id.main_pb);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
