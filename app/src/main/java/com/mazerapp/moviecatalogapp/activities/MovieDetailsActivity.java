package com.mazerapp.moviecatalogapp.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.adapters.MovieReviewAdapter;
import com.mazerapp.moviecatalogapp.adapters.MovieTrailersAdapter;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieDetails;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieReviews;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieTrailers;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieDetails;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieReviews;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieTrailers;
import com.mazerapp.moviecatalogapp.repositories.MovieRepository;
import com.mazerapp.moviecatalogapp.utils.Constants;
import com.mazerapp.moviecatalogapp.utils.Util;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_NO_CONNECTION;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_WITH_SERVICE;
import static com.mazerapp.moviecatalogapp.utils.Constants.YOUTUBE_BASE_URL;

public class MovieDetailsActivity extends AppCompatActivity implements OnGetMovieTrailers, OnGetMovieReviews, SwipeRefreshLayout.OnRefreshListener {


    private String movieId;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle extras = getIntent().getExtras();

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

        mTrailerAdapter = new MovieTrailersAdapter(new MovieTrailersAdapter.OnClickItem() {
            @Override
            public void onTrailerClicked(MovieTrailers.TrailerInfo trailerInfo) {
                String youtubeUrl = YOUTUBE_BASE_URL + trailerInfo.getKey();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
            }
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

            getMovieDetails(this.movieId);
        }

    }

    private void getMovieTrailers(String id){
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.getMovieTrailers(id, this);
        progressTrailers.setVisibility(View.VISIBLE);
        rvTrailers.setVisibility(View.GONE);
        tvNoTrailers.setVisibility(View.GONE);
    }

    private void getMovieReviews(String id){
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.getMovieReviews(id, this);
        progressReviews.setVisibility(View.VISIBLE);
        tvNoReviews.setVisibility(View.GONE);
        tvNoTrailers.setVisibility(View.GONE);
    }

    private void getMovieDetails(final String id){
        swipeLayout.setRefreshing(true);
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.getMovieDetails(id, new OnGetMovieDetails() {
            @Override
            public void onSuccess(MovieDetails movieDetails) {
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
                contentPanel.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(int code) {
                swipeLayout.setRefreshing(false);
                switch (code){
                    case ERROR_NO_CONNECTION:
                        frameNoConnection.setVisibility(View.VISIBLE);
                        contentPanel.setVisibility(View.GONE);
                        break;
                    case ERROR_WITH_SERVICE:
                        Util.showDialog(getApplicationContext(), getApplicationContext().getString(R.string.movie_list_not_found_titulo), getApplicationContext().getString(R.string.movie_list_not_found_text));
                        break;
                    default:
                        Util.showDialog(getApplicationContext(), getApplicationContext().getString(R.string.movie_list_not_found_titulo), getApplicationContext().getString(R.string.movie_list_not_found_text));
                        break;
                }
            }
        });
    }


    @Override
    public void onGetTrailersSuccess(MovieTrailers movieTrailers) {
        mTrailerAdapter.setListTrailers(movieTrailers.getResults());
        progressTrailers.setVisibility(View.GONE);
        if (!movieTrailers.getResults().isEmpty()) {
            rvTrailers.setVisibility(View.VISIBLE);
            tvNoTrailers.setVisibility(View.GONE);
        }else{
            rvTrailers.setVisibility(View.GONE);
            tvNoTrailers.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onGetTrailersFailure(int code) {
        progressTrailers.setVisibility(View.GONE);
        rvTrailers.setVisibility(View.GONE);
        tvNoTrailers.setText(getString(R.string.fail_trailer));
        tvNoTrailers.setVisibility(View.VISIBLE);

    }

    @Override
    public void onGetReviewsSuccess(MovieReviews reviews) {
        mReviewAdapter.setListReviews(reviews.getResults());
        progressReviews.setVisibility(View.GONE);
        if (!reviews.getResults().isEmpty()){
            rvReviews.setVisibility(View.VISIBLE);
            tvNoReviews.setVisibility(View.GONE);
        }else{
            rvReviews.setVisibility(View.GONE);
            tvNoReviews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetReviewFailure(int code) {
        progressReviews.setVisibility(View.GONE);
        tvNoReviews.setText(getString(R.string.fail_reviews));
        tvNoReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        getMovieDetails(this.movieId);
    }
}
