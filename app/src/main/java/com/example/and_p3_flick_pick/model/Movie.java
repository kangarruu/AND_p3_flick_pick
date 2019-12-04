package com.example.and_p3_flick_pick.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movies")
public class Movie implements Parcelable {

    @PrimaryKey
    private int movieId;                       //movie id and primary key for Room
    private String title;                       //title
    private String posterPath;                  //movie poster image path
    private String backdropPath;                //movie backdrop image path
    private String overview;                    //A plot synopsis (called overview in the api)
    private double rating;                      //user rating (called vote_average in the api)
    private String releaseDate;                 //release date



    //Constructor in case other details are missing from TMDB
    @Ignore
    public Movie(String title){
        this.title = title;
    }

    public Movie(int id, String title, String imagePath, String backdropPath, String overview, double rating, String date){
        this.movieId = id;
        this.title = title;
        this.posterPath = imagePath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = date;
    }

    private Movie(Parcel in) {
        movieId = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", overview='" + overview + '\'' +
                ", rating=" + rating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
