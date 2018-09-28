package com.mazerapp.moviecatalogapp.repositories;

import android.os.AsyncTask;

import com.mazerapp.moviecatalogapp.interfaces.OnConnectionChecked;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieDetails;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieList;
import com.mazerapp.moviecatalogapp.interfaces.retrofit.MovieService;
import com.mazerapp.moviecatalogapp.models.retrofit.Movie;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieDetails;
import com.mazerapp.moviecatalogapp.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mazerapp.moviecatalogapp.utils.Constants.API_KEY;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_NO_CONNECTION;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_WITH_SERVICE;

/**
 * Created by arthur on 13/09/2018.
 */

public class MovieRepository {

    private MovieService movieService;

    public MovieRepository(){
        movieService = NetworkUtils.buildUrl().create(MovieService.class);
    }

    public void getListOfMovies(final OnGetMovieList onGetMovieList){
        InternetCheck internetCheck = new InternetCheck();
        internetCheck.execute(new OnConnectionChecked() {
            @Override
            public void isConnected() {
                movieService.listMostPopular(API_KEY)
                        .enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                                onGetMovieList.onSuccess(response.body());
                            }

                            @Override
                            public void onFailure(@NotNull Call<Movie> call, @NotNull Throwable t) {
                                onGetMovieList.onFailure(ERROR_WITH_SERVICE);
                            }
                        });
            }

            @Override
            public void isNotConnected() {
                onGetMovieList.onFailure(ERROR_NO_CONNECTION);
            }
        });


    }
    public void getListOfTopRated(final OnGetMovieList onGetMovieList){
        InternetCheck internetCheck = new InternetCheck();
        internetCheck.execute(new OnConnectionChecked() {
            @Override
            public void isConnected() {
                movieService.listTopRated(API_KEY)
                        .enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                                onGetMovieList.onSuccess(response.body());
                            }

                            @Override
                            public void onFailure(@NotNull Call<Movie> call, @NotNull Throwable t) {
                                onGetMovieList.onFailure(ERROR_WITH_SERVICE);
                            }
                        });
            }

            @Override
            public void isNotConnected() {
                onGetMovieList.onFailure(ERROR_NO_CONNECTION);
            }
        });
    }


    public void getMovieDetails(final String id , final OnGetMovieDetails onGetMovieDetails){
        InternetCheck internetCheck = new InternetCheck();
        internetCheck.execute(new OnConnectionChecked() {
            @Override
            public void isConnected() {
                movieService.getMovieDetails(id, API_KEY)
                        .enqueue(new Callback<MovieDetails>() {
                            @Override
                            public void onResponse(@NotNull Call<MovieDetails> call, @NotNull Response<MovieDetails> response) {
                                onGetMovieDetails.onSuccess(response.body());
                            }

                            @Override
                            public void onFailure(@NotNull Call<MovieDetails> call, @NotNull Throwable t) {
                                onGetMovieDetails.onFailure(ERROR_WITH_SERVICE);
                            }
                        });
            }

            @Override
            public void isNotConnected() {
                onGetMovieDetails.onFailure(ERROR_NO_CONNECTION);
            }
        });


    }

    class InternetCheck extends AsyncTask<OnConnectionChecked, Void, Boolean> {

        private OnConnectionChecked onConnectionChecked;

        @Override
        protected Boolean doInBackground(OnConnectionChecked... onConnectionCheckeds) {
            if (onConnectionCheckeds[0] != null)
                onConnectionChecked = onConnectionCheckeds[0];
            else
                return false;
            return NetworkUtils.isInternetConnected();
        }

        @Override
        protected void onPostExecute(Boolean isConnected){
            if (isConnected)
                onConnectionChecked.isConnected();
            else
                onConnectionChecked.isNotConnected();
        }
    }
}
