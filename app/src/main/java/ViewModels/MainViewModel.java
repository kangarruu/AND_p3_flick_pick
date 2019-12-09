package ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.and_p3_flick_pick.model.Movie;

import java.util.List;

import database.MovieDatabase;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDb = MovieDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Actively retrieving favorite movies from the database");
        favorites = movieDb.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getFavorites(){
        return favorites;
    }
}
