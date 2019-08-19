import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

public class Movie implements Parcelable {
    private String title;                       //original title
    private String posterImage;                 // movie poster image thumbnail
    private String overview;                    //A plot synopsis (called overview in the api)
    private double rating;                      //user rating (called vote_average in the api)
    private String releaseDate;                 //release date

    public Movie(String title, String image, String overview, double rating, String date){
        this.title = title;
        this.posterImage = image;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = date;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        posterImage = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();

    }

    public String getTitle() {
        return title;
    }

    public String getPosterImage() {
        return posterImage;
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
