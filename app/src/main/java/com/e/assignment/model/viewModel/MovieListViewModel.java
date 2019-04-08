package com.e.assignment.model.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.e.assignment.model.Movie;
import com.e.assignment.model.MovieModel;
import com.e.assignment.model.MovieModelImpl;

import java.util.Map;

public class MovieListViewModel extends AndroidViewModel {
    //movie list
    private MutableLiveData<Map<String, Movie>> movieLiveData;
    public MovieListViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Map<String, Movie>> getMovie(){
        if(movieLiveData == null){
            movieLiveData = new MutableLiveData<>();
            MovieModel movies = MovieModelImpl.getSingletonInstance(getApplication());
            //load data
            movieLiveData.setValue(movies.getMovieList());
        }
        return movieLiveData;
    }
}
