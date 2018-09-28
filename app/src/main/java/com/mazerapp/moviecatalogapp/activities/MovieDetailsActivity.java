package com.mazerapp.moviecatalogapp.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieDetails;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieDetails;
import com.mazerapp.moviecatalogapp.repositories.MovieRepository;
import com.mazerapp.moviecatalogapp.utils.Constants;
import com.mazerapp.moviecatalogapp.utils.Util;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_NO_CONNECTION;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_WITH_SERVICE;

public class MovieDetailsActivity extends AppCompatActivity {


    private String movieId;
    private TextView tvMovieTitle;
    private TextView tvYearMovie;
    private TextView tvMovieDuration;
    private TextView tvRatingMovie;
    private TextView tvMovieDescription;
    private ImageView ivPoster;
    private ImageView ivMiniPoster;
    private LinearLayout loadingProgress;
    private RelativeLayout contentPanel;
    private FrameLayout frameNoConnection;


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
        loadingProgress = findViewById(R.id.loading_progress);
        contentPanel = findViewById(R.id.content_panel);
        frameNoConnection = findViewById(R.id.frame_no_connection);

        //coloring progressbar with green
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.lightGreen), PorterDuff.Mode.SRC_IN);

        if (extras != null){
            this.movieId = getIntent().getStringExtra(Constants.MOVIE_ID_EXTRA);

            tvMovieTitle.setText(getIntent().getStringExtra(Constants.MOVIE_TITLE_EXTRA));
            String urlPoster = Constants.MOVIEES_DB_BASE_IMG_URL + Constants.MOVIES_DB_IMG_SIZES[4] + getIntent().getStringExtra(Constants.MOVIE_POSTER_PATH_EXTRA);
            Picasso.with(getApplicationContext()).load(urlPoster).into(ivPoster);

            getMovieDetails(this.movieId);
        }

    }

    private void getMovieDetails(String id){
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.getMovieDetails(id, new OnGetMovieDetails() {
            @Override
            public void onSuccess(MovieDetails movieDetails) {

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

                loadingProgress.setVisibility(View.GONE);
                contentPanel.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(int code) {
                switch (code){
                    case ERROR_NO_CONNECTION:
                        frameNoConnection.setVisibility(View.VISIBLE);
                        contentPanel.setVisibility(View.GONE);
                        loadingProgress.setVisibility(View.GONE);
                        break;
                    case ERROR_WITH_SERVICE:
                        Util.showDialog(getApplicationContext(), getApplicationContext().getString(R.string.movie_list_not_found_titulo), getApplicationContext().getString(R.string.movie_list_not_found_text));
                        loadingProgress.setVisibility(View.GONE);
                        break;
                    default:
                        Util.showDialog(getApplicationContext(), getApplicationContext().getString(R.string.movie_list_not_found_titulo), getApplicationContext().getString(R.string.movie_list_not_found_text));
                        loadingProgress.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }


}
