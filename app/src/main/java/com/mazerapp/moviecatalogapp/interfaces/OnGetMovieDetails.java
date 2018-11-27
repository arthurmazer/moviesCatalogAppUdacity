package com.mazerapp.moviecatalogapp.interfaces;

import com.mazerapp.moviecatalogapp.models.MovieDetails;

public interface OnGetMovieDetails {

    void onSuccess(MovieDetails movieDetails);
    void onFailure(int code);

}
