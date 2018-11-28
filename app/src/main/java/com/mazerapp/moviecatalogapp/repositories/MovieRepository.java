package com.mazerapp.moviecatalogapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.mazerapp.moviecatalogapp.interfaces.retrofit.MovieService;
import com.mazerapp.moviecatalogapp.models.Movie;
import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.models.MovieFav;
import com.mazerapp.moviecatalogapp.models.MovieReviews;
import com.mazerapp.moviecatalogapp.models.MovieTrailers;
import com.mazerapp.moviecatalogapp.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mazerapp.moviecatalogapp.utils.Constants.API_KEY;

/**
 * Created by arthur on 13/09/2018.
 */

public class MovieRepository {

    private MovieService movieService;
    private DbManager dbManager;

    public MovieRepository(Context ctx){
        movieService = NetworkUtils.buildUrl().create(MovieService.class);
        dbManager = DbManager.getInstance(ctx);
    }

    public LiveData<Boolean> isFavorite(String idMovie){
       return dbManager.movieDao().checkMovieIsFavorite(idMovie);
    }

    public void setMovieAsFavorite(String idMovie, boolean isFavorite){
       dbManager.movieDao().setMovieFavorite(new MovieFav(idMovie, isFavorite));
    }

    public LiveData<List<MovieDetails>> getFavoriteMovies(){
        return dbManager.movieDao().getFavoriteMovies();
    }

    public LiveData<Movie> getListOfMovies(){
        final MutableLiveData<Movie> movieDetailsData = new MutableLiveData<>();
        movieService.listMostPopular(API_KEY)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                        movieDetailsData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<Movie> call, @NotNull Throwable t) {
                        movieDetailsData.setValue(null);
                    }
                });
            return movieDetailsData;
    }

    public LiveData<Movie> getListOfTopRated(){
        final MutableLiveData<Movie> movieDetailsData = new MutableLiveData<>();
        movieService.listTopRated(API_KEY)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                        movieDetailsData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<Movie> call, @NotNull Throwable t) {
                        movieDetailsData.setValue(null);
                    }
                });
        return movieDetailsData;
    }


    public LiveData<MovieDetails> getMovieDetails(final String id, boolean forceRefresh){
        final MutableLiveData<MovieDetails> movieDetailsData = new MutableLiveData<>();
        LiveData<MovieDetails> movieDetailsRoomData = dbManager.movieDao().getMovieDetailsById(id);
        MovieDetails hasMovieDetails = dbManager.movieDao().hasMovieDetails(id);

        //Ta no DB Local
        if (hasMovieDetails != null && !forceRefresh)
            return movieDetailsRoomData;

        //Nao tem registro local, consulta API
        movieService.getMovieDetails(id, API_KEY)
                .enqueue(new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieDetails> call, @NotNull Response<MovieDetails> response) {
                        if ( response.body() != null ) {
                            MovieDetails movieDetails = response.body();
                            movieDetails.setIdMovie(id);
                            dbManager.movieDao().insertMovieFav(movieDetails);
                            movieDetailsData.setValue(movieDetails);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<MovieDetails> call, @NotNull Throwable t) {
                        movieDetailsData.setValue(null);
                    }
                });

        return movieDetailsData;
    }

    public LiveData<MovieTrailers> getMovieTrailers(final String id){
        final MutableLiveData<MovieTrailers> movieTrailesData = new MutableLiveData<>();

        movieService.getTrailers(id, API_KEY)
                .enqueue(new Callback<MovieTrailers>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieTrailers> call, @NotNull Response<MovieTrailers> response) {
                        movieTrailesData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<MovieTrailers> call, @NotNull Throwable t) {
                        movieTrailesData.setValue(null);
                    }
                });

        return movieTrailesData;
    }

    public LiveData<MovieReviews> getMovieReviews(final String id){
        final MutableLiveData<MovieReviews> movieReviews = new MutableLiveData<>();

        movieService.getReviews(id, API_KEY)
                .enqueue(new Callback<MovieReviews>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieReviews> call, @NotNull Response<MovieReviews> response) {
                        movieReviews.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<MovieReviews> call, @NotNull Throwable t) {
                        movieReviews.setValue(null);
                    }
                });

        return movieReviews;
    }
}



