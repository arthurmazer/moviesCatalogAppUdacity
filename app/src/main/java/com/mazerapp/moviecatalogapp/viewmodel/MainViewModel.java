package com.mazerapp.moviecatalogapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mazerapp.moviecatalogapp.models.Movie;
import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.repositories.MovieRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private MovieRepository movieRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public LiveData<Movie> getMostPopularMovieList(){
        return movieRepository.getListOfMovies();
    }

    public LiveData<Movie> getTopRatedMovieList(){
        return movieRepository.getListOfTopRated();
    }

    public LiveData<List<MovieDetails>> getFavoriteMovies(){
        return movieRepository.getFavoriteMovies();
    }
}
