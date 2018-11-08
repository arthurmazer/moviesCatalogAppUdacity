package com.mazerapp.moviecatalogapp.interfaces;

import com.mazerapp.moviecatalogapp.models.retrofit.MovieReviews;

public interface OnGetMovieReviews {
    void onGetReviewsSuccess(MovieReviews movie);
    void onGetReviewFailure(int code);
}
