package com.mazerapp.moviecatalogapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.adapters.MovieReviewAdapter;
import com.mazerapp.moviecatalogapp.adapters.MovieTrailersAdapter;
import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.utils.Constants;
import com.mazerapp.moviecatalogapp.utils.Util;
import com.mazerapp.moviecatalogapp.viewmodel.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import static com.mazerapp.moviecatalogapp.utils.Constants.YOUTUBE_BASE_URL;

public class MovieDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tvMovieTitle;
    private TextView tvYearMovie;
    private TextView tvMovieDuration;
    private TextView tvRatingMovie;
    private TextView tvMovieDescription;
    private TextView tvNoTrailers;
    private TextView tvNoReviews;
    private ImageView ivPoster;
    private ImageView ivMiniPoster;
    private RelativeLayout contentPanel;
    private FrameLayout frameNoConnection;
    private RecyclerView rvTrailers;
    private RecyclerView rvReviews;
    private MovieReviewAdapter mReviewAdapter;
    private MovieTrailersAdapter mTrailerAdapter;
    private SwipeRefreshLayout swipeLayout;

    private ProgressBar progressTrailers;
    private ProgressBar progressReviews;

    private String movieId;
    private boolean isFavorite;
    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle extras = getIntent().getExtras();

        //init view model
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvYearMovie = findViewById(R.id.tv_release_date);
        tvMovieDuration = findViewById(R.id.tv_movie_duration);
        tvRatingMovie = findViewById(R.id.tv_rating_movie);
        tvMovieDescription = findViewById(R.id.tv_movie_description);
        ivPoster = findViewById(R.id.iv_poster_header);
        ivMiniPoster = findViewById(R.id.iv_min_poster);
        contentPanel = findViewById(R.id.content_panel);
        frameNoConnection = findViewById(R.id.frame_no_connection);
        rvTrailers = findViewById(R.id.rv_trailers);
        rvReviews = findViewById(R.id.rv_reviews);
        mReviewAdapter = new MovieReviewAdapter();
        swipeLayout = findViewById(R.id.swipe_layout);
        tvNoTrailers = findViewById(R.id.tv_no_trailers);
        tvNoReviews = findViewById(R.id.tv_no_reviews);
        progressTrailers = findViewById(R.id.progress_trailers);
        progressReviews = findViewById(R.id.progress_reviews);

        progressTrailers.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        progressReviews.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        mTrailerAdapter = new MovieTrailersAdapter( trailerInfo -> {
            String youtubeUrl = YOUTUBE_BASE_URL + trailerInfo.getKey();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
        });

        rvTrailers.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.setItemAnimator(new DefaultItemAnimator());
        rvTrailers.setAdapter(mTrailerAdapter);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setItemAnimator(new DefaultItemAnimator());
        rvReviews.setAdapter(mReviewAdapter);

        swipeLayout.setOnRefreshListener(this);

        if (extras != null){
            this.movieId = getIntent().getStringExtra(Constants.MOVIE_ID_EXTRA);

            tvMovieTitle.setText(getIntent().getStringExtra(Constants.MOVIE_TITLE_EXTRA));
            String urlPoster = Constants.MOVIEES_DB_BASE_IMG_URL + Constants.MOVIES_DB_IMG_SIZES[4] + getIntent().getStringExtra(Constants.MOVIE_POSTER_PATH_EXTRA);
            Picasso.with(getApplicationContext()).load(urlPoster).into(ivPoster);

            getMovieDetails(this.movieId, false);
        }

    }

    private void getMovieTrailers(String id){
        progressTrailers.setVisibility(View.VISIBLE);
        rvTrailers.setVisibility(View.GONE);
        tvNoTrailers.setVisibility(View.GONE);
        movieDetailsViewModel.getMovieTrailers(id).observe(this, movieTrailers -> {
            if (movieTrailers != null){
                mTrailerAdapter.setListTrailers(movieTrailers.getResults());
                progressTrailers.setVisibility(View.GONE);
                if (!movieTrailers.getResults().isEmpty()) {
                    rvTrailers.setVisibility(View.VISIBLE);
                    tvNoTrailers.setVisibility(View.GONE);
                }else{
                    rvTrailers.setVisibility(View.GONE);
                    tvNoTrailers.setVisibility(View.VISIBLE);
                }
            }else{
                progressTrailers.setVisibility(View.GONE);
                rvTrailers.setVisibility(View.GONE);
                tvNoTrailers.setText(getString(R.string.fail_trailer));
                tvNoTrailers.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getMovieReviews(String id){
        progressReviews.setVisibility(View.VISIBLE);
        tvNoReviews.setVisibility(View.GONE);
        tvNoTrailers.setVisibility(View.GONE);

        movieDetailsViewModel.getMovieReviews(id).observe(this, movieReviews -> {
            if (movieReviews != null){
                mReviewAdapter.setListReviews(movieReviews.getResults());
                progressReviews.setVisibility(View.GONE);
                if (!movieReviews.getResults().isEmpty()){
                    rvReviews.setVisibility(View.VISIBLE);
                    tvNoReviews.setVisibility(View.GONE);
                }else{
                    rvReviews.setVisibility(View.GONE);
                    tvNoReviews.setVisibility(View.VISIBLE);
                }
            }else{
                progressReviews.setVisibility(View.GONE);
                tvNoReviews.setText(getString(R.string.fail_reviews));
                tvNoReviews.setVisibility(View.VISIBLE);
            }

        });
    }

    private void getMovieDetails(final String id, boolean forceRefresh){
        swipeLayout.setRefreshing(true);

        Observer observer = (Observer<MovieDetails>) movieDetails -> {
            if(movieDetails != null) {
                contentPanel.setVisibility(View.VISIBLE);
                getMovieTrailers(id);
                getMovieReviews(id);
                frameNoConnection.setVisibility(View.GONE);

                try {
                    tvYearMovie.setText(Util.dateFormat(movieDetails.getReleaseDate()));
                } catch (ParseException e) {
                    tvYearMovie.setText(movieDetails.getReleaseDate());
                }
                tvMovieDuration.setText(movieDetails.getRuntime() + "min");
                tvRatingMovie.setText(String.valueOf(movieDetails.getVoteAverage()));
                tvMovieDescription.setText(movieDetails.getOverview());

                String urlPoster = Constants.MOVIEES_DB_BASE_IMG_URL + Constants.MOVIES_DB_IMG_SIZES[4] + getIntent().getStringExtra(Constants.MOVIE_POSTER_PATH_EXTRA);
                Picasso.with(getApplicationContext()).load(urlPoster).into(ivMiniPoster);

                swipeLayout.setRefreshing(false);
            }else{
                swipeLayout.setRefreshing(false);
                frameNoConnection.setVisibility(View.VISIBLE);
                contentPanel.setVisibility(View.GONE);
            }


            movieDetailsViewModel.getMovieDetails(id, forceRefresh).removeObservers(this);
        };

        movieDetailsViewModel.getMovieDetails(id, forceRefresh).observe(this, observer);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);

        movieDetailsViewModel.isFavorite(movieId).observe(this, isFavorite -> {
            if(isFavorite == null || !isFavorite) {
                menu.getItem(1).setIcon(R.drawable.ic_favorite_border_white_24dp);
                this.isFavorite = false;
            }else{
                menu.getItem(1).setIcon(R.drawable.ic_favorite_red_24dp);
                this.isFavorite = true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(!this.isFavorite) {
                    item.setIcon(R.drawable.ic_favorite_red_24dp);
                    movieDetailsViewModel.setFavorite(movieId,true);
                }else{
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    movieDetailsViewModel.setFavorite(movieId,false);
                }

                return true;
            case R.id.action_refresh:
                doRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doRefresh(){
        getMovieDetails(this.movieId, true);
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }
}
