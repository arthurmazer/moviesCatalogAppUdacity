package com.mazerapp.moviecatalogapp.interfaces;

import com.mazerapp.moviecatalogapp.models.retrofit.MovieDetails;

public interface OnGetMovieDetails {

    void onSuccess(MovieDetails movieDetails);
    void onFailure(int code);

}
