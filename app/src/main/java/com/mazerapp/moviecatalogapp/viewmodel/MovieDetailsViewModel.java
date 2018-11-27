package com.mazerapp.moviecatalogapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.models.MovieReviews;
import com.mazerapp.moviecatalogapp.models.MovieTrailers;
import com.mazerapp.moviecatalogapp.repositories.MovieRepository;

public class MovieDetailsViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public LiveData<MovieDetails> getMovieDetails(String id, boolean forceRefresh) {
       return movieRepository.getMovieDetails(id, forceRefresh);
    }

    public LiveData<MovieTrailers> getMovieTrailers(String id){
        return movieRepository.getMovieTrailers(id);
    }

    public LiveData<MovieReviews> getMovieReviews(String id){
        return movieRepository.getMovieReviews(id);
    }

    public LiveData<Boolean> isFavorite(String id){
        return movieRepository.isFavorite(id);
    }

    public void setFavorite(String id, boolean isFavorite){
        movieRepository.setMovieAsFavorite(id, isFavorite);
    }


}
