package com.mazerapp.moviecatalogapp.interfaces;

import com.mazerapp.moviecatalogapp.models.retrofit.MovieTrailers;

public interface OnGetMovieTrailers {

    void onGetTrailersSuccess(MovieTrailers movie);
    void onGetTrailersFailure(int code);
}
