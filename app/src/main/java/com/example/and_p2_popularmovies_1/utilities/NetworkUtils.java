package com.example.and_p2_popularmovies_1.utilities;

import android.app.Activity;
import android.content.Context;

import com.example.and_p2_popularmovies_1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String QUERY_BASE_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String QUERY_BASE_RATING = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    //Method for making the HTTP request and returning the response
    //@param the URL for making the HTTP request
    //@return the contents of the HTTP response
    public static String getHttpResponse(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }else {
                return null;
            }
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
