package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.and_p3_flick_pick.model.Movie;

import java.util.ArrayList;
import java.util.List;

//Dao interface for reading and writing movies to/from the database
@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movies")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM Movies WHERE movieId = :movieId")
    LiveData<Movie> loadMovieById(int movieId);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
