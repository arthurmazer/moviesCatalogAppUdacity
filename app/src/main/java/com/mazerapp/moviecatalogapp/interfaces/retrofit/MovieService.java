package com.mazerapp.moviecatalogapp.interfaces.retrofit;

import com.mazerapp.moviecatalogapp.models.retrofit.Movie;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieDetails;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieTrailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by arthur on 09/09/2018.
 */

public interface MovieService {

    @GET("/3/movie/popular")
    Call<Movie> listMostPopular(@Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<Movie> listTopRated(@Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") String movieId,
                                       @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/videos")
    Call<MovieTrailers> getTrailers(@Path("movie_id") String movieId,
                                    @Query("api_key") String apiKey);

}
