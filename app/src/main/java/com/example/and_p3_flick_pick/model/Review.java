package com.example.and_p3_flick_pick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Review implements Parcelable {

    @SerializedName("author")
    @Expose
    private String author;                     //author

    @SerializedName("id")
    @Expose
    private String id;                         //review id

    @SerializedName("content")
    @Expose
    private String content;                    //review text

    @SerializedName("url")
    @Expose
    private String url;                        //url to review in TMDB


//    public Review(int movieId, String author, String content, String url) {
//        this.movieId = movieId;
//        this.author = author;
//        this.content = content;
//        this.url = url;
//    }
//
//    protected Review(Parcel in) {
//        movieId = in.readInt();
//        author = in.readString();
//        content = in.readString();
//        url = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(movieId);
//        dest.writeString(author);
//        dest.writeString(content);
//        dest.writeString(url);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<Review> CREATOR = new Creator<Review>() {
//        @Override
//        public Review createFromParcel(Parcel in) {
//            return new Review(in);
//        }
//
//        @Override
//        public Review[] newArray(int size) {
//            return new Review[size];
//        }
//    };
//
//    //Getters
//    public int getMovieId() {
//        return movieId;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    @Override
//    public String toString() {
//        return "Review{" +
//                "movieId=" + movieId +
//                ", author='" + author + '\'' +
//                ", content='" + content + '\'' +
//                ", url='" + url + '\'' +
//                '}';
//    }


    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }


    protected Review(Parcel in) {
        author = in.readString();
        id = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
