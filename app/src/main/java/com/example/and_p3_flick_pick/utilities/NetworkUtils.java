package com.example.and_p3_flick_pick.utilities;

import android.util.Log;

import com.example.and_p3_flick_pick.BuildConfig;
import com.example.and_p3_flick_pick.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static ArrayList<Movie> parsedMovieList;
    private static URL sortUrl = null;

    private static final String RESULTS_KEY = "results";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String TITLE_KEY = "title";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String OVERVIEW_KEY = "overview";
    private static final String DATE_KEY = "release_date";
    private static final String ID = "id";


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

    //Method for parsing the JSON response String from getHttpResponse()
    //@param a String corresponding to the Http response
    //@return an ArrayList of Movie objects
    public static ArrayList<Movie> parseJsonResponse(String httpResponse){
        parsedMovieList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(httpResponse);
            JSONArray resultsArray = baseJsonResponse.getJSONArray(RESULTS_KEY);
            //iterate through the JSONArray and extract necessary Movie elements
            for (int i = 0; i < resultsArray.length(); i++){
                JSONObject result = resultsArray.getJSONObject(i);
                String posterPath = result.optString(POSTER_PATH_KEY);
                int id = result.optInt(ID);
                String title = result.optString(TITLE_KEY);
                double rating = result.optDouble(VOTE_AVERAGE_KEY);
                String overview = result.optString(OVERVIEW_KEY);
                String releaseDate = result.optString(DATE_KEY);

                //Create a Movie object and append it to parsedMovieList
                Movie movie = new Movie(id,title, posterPath, overview, rating, releaseDate);
                parsedMovieList.add(movie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Issues parsing Movie JSON results", e);
        }
        return parsedMovieList;
    }

    public static URL buildUrl(String sort){
        try {
            sortUrl = new URL(sort + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } return sortUrl;
    }

}
