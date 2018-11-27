package com.mazerapp.moviecatalogapp.interfaces.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.models.MovieFav;

@Dao
public interface MovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieFav(MovieDetails movieFav);


    @Query("SELECT * FROM movies_details WHERE idMovie = :idMovie")
    LiveData<MovieDetails> getMovieDetailsById(String idMovie);

    @Query("SELECT * FROM movies_details WHERE idMovie = :idMovie")
    MovieDetails hasMovieDetails(String idMovie);

    @Query("SELECT isFavorite FROM movies_favorite WHERE id = :idMovie")
    LiveData<Boolean> checkMovieIsFavorite(String idMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setMovieFavorite(MovieFav movieFav);



}
