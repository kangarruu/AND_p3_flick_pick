import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

public class Movie implements Parcelable {
    String title;                       //original title
    URL posterImage;                    // movie poster image thumbnail
    String overview;                    //A plot synopsis (called overview in the api)
    double rating;                      //user rating (called vote_average in the api)
    String releaseDate;                 //release date

    public Movie(String title, URL image, String overview, double rating, String date){
        this.title = title;
        this.posterImage = image;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = date;
    }

    protected Movie(Parcel in) {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(String.valueOf(posterImage));
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
}
