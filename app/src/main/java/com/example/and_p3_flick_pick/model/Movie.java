package com.example.and_p3_flick_pick.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//Entity annotation for Room
@Entity(tableName = "Movies")
public class Movie implements Parcelable {

    @PrimaryKey
    private int movieId;                        //movie id and primary key for Room
    private String title;                       //title
    private String posterPath;                  //movie poster image path
    private String backdropPath;                //movie backdrop image path
    private String overview;                    //A plot synopsis (called overview in the api)
    private double rating;                      //user rating (called vote_average in the api)
    private String releaseDate;                 //release date
    private boolean isFavorite;                 //Has movie been selected as favorite


    //Constructor in case other details are missing from TMDB
    public Movie() {    }

    public Movie(int id, String title, String imagePath, String backdropPath, String overview, double rating, String date, boolean isFavorite){
        this.movieId = id;
        this.title = title;
        this.posterPath = imagePath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = date;
        this.isFavorite = isFavorite;
    }

    private Movie(Parcel in) {
        movieId = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        isFavorite = in.readInt() == 1;
    }

    //Getter methods:
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

    public boolean getFavorite() {
        return isFavorite;
    }


    //Setter methods:
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
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
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
        parcel.writeInt(isFavorite ? 1 : 0);
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

    @NonNull
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
                ", isFavorite=" + isFavorite +
                '}';
    }
}
