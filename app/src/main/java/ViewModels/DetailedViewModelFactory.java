package ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import database.MovieDatabase;
//Factory for creating DetailedViewModel
public class DetailedViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static final String LOG_TAG = DetailedViewModelFactory.class.getSimpleName();

    private final MovieDatabase mDb;
    private final int mMovieId;

    public DetailedViewModelFactory(MovieDatabase mDb, int mMovieId) {
        this.mDb = mDb;
        this.mMovieId = mMovieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailedViewModel(mDb,mMovieId);
    }
}
