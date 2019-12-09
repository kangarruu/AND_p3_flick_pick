package ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.and_p3_flick_pick.model.Movie;

import database.MovieDatabase;

public class DetailedViewModel extends ViewModel {

    private static final String LOG_TAG = DetailedViewModel.class.getSimpleName();

    private LiveData<Movie> movie;

    public DetailedViewModel(MovieDatabase database, int id) {
        movie = database.movieDao().loadMovieById(id);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
