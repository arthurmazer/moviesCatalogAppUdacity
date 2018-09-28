package com.mazerapp.moviecatalogapp.interfaces;

import com.mazerapp.moviecatalogapp.models.retrofit.Movie;

/**
 * Created by arthur on 13/09/2018.
 */

public interface OnGetMovieList {

    void onSuccess(Movie movie);
    void onFailure(int code);
}
