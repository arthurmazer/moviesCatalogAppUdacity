package com.mazerapp.moviecatalogapp.interfaces;

import com.mazerapp.moviecatalogapp.models.MovieReviews;

public interface OnGetMovieReviews {
    void onGetReviewsSuccess(MovieReviews movie);
    void onGetReviewFailure(int code);
}
