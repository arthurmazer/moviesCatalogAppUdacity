package com.mazerapp.moviecatalogapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.adapters.MovieCatalogAdapter;
import com.mazerapp.moviecatalogapp.interfaces.OnGetMovieList;
import com.mazerapp.moviecatalogapp.models.retrofit.Movie;
import com.mazerapp.moviecatalogapp.repositories.MovieRepository;
import com.mazerapp.moviecatalogapp.utils.Constants;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_NO_CONNECTION;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_WITH_SERVICE;

import com.mazerapp.moviecatalogapp.utils.Util;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieCatalogAdapter movieCatalogAdapter;
    private ArrayList<Movie.MovieInfo> listOfMovies;
    private MovieRepository movieRepository;
    private Context context;
    private LinearLayout mLoadingView;
    private FrameLayout frameNoConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.subitem_most_popular));

        listOfMovies = new ArrayList<>();
        context = getApplicationContext();

        //inject
        mLoadingView = findViewById(R.id.loading_progress);
        rvMovies = findViewById(R.id.rv_movie_catalog);
        frameNoConnection = findViewById(R.id.frame_no_connection);

        //coloring progressbar with green
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.lightGreen), PorterDuff.Mode.SRC_IN);

        //initiate the adapter and set on click to items
        movieCatalogAdapter = new MovieCatalogAdapter(listOfMovies, new MovieCatalogAdapter.OnClickItem() {
            @Override
            public void onMovieClicked(Movie.MovieInfo movie) {
                Intent it = new Intent(context, MovieDetailsActivity.class);
                it.putExtra(Constants.MOVIE_ID_EXTRA, movie.getId());
                it.putExtra(Constants.MOVIE_TITLE_EXTRA, movie.getTitle());
                it.putExtra(Constants.MOVIE_POSTER_PATH_EXTRA, movie.getPosterPath());
                startActivity(it);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(mLayoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setAdapter(movieCatalogAdapter);

        //load initial data
        getMostPopularMovieList();
    }


    private void getMostPopularMovieList() {
        rvMovies.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        movieRepository = new MovieRepository();
        movieRepository.getListOfMovies(new OnGetMovieList() {
            @Override
            public void onSuccess(Movie movie) {
                frameNoConnection.setVisibility(View.GONE);
                setTitle(getString(R.string.subitem_most_popular));
                listOfMovies.clear();
                listOfMovies.addAll(movie.getMoviesInfoList());
                movieCatalogAdapter.notifyDataSetChanged();
                mLoadingView.setVisibility(View.GONE);
                rvMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int code) {
                switch (code){
                    case ERROR_NO_CONNECTION:
                        frameNoConnection.setVisibility(View.VISIBLE);
                        rvMovies.setVisibility(View.GONE);
                        mLoadingView.setVisibility(View.GONE);
                        break;
                    case ERROR_WITH_SERVICE:
                        Util.showDialog(context, context.getString(R.string.movie_list_not_found_titulo), context.getString(R.string.movie_list_not_found_text));
                        mLoadingView.setVisibility(View.GONE);
                        break;
                    default:
                        Util.showDialog(context, context.getString(R.string.movie_list_not_found_titulo), context.getString(R.string.movie_list_not_found_text));
                        mLoadingView.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void getTopRatedMovieList() {
        rvMovies.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        movieRepository = new MovieRepository();
        movieRepository.getListOfTopRated(new OnGetMovieList() {
            @Override
            public void onSuccess(Movie movie) {
                setTitle(getString(R.string.subitem_top_rating));
                frameNoConnection.setVisibility(View.GONE);
                listOfMovies.clear();
                listOfMovies.addAll(movie.getMoviesInfoList());
                movieCatalogAdapter.notifyDataSetChanged();
                mLoadingView.setVisibility(View.GONE);
                rvMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int code) {
                switch (code){
                    case ERROR_NO_CONNECTION:
                        frameNoConnection.setVisibility(View.VISIBLE);
                        rvMovies.setVisibility(View.GONE);
                        mLoadingView.setVisibility(View.GONE);
                        break;
                    case ERROR_WITH_SERVICE:
                        Util.showDialog(context, context.getString(R.string.movie_list_not_found_titulo), context.getString(R.string.movie_list_not_found_text));
                        mLoadingView.setVisibility(View.GONE);
                        break;
                    default:
                        Util.showDialog(context, context.getString(R.string.movie_list_not_found_titulo), context.getString(R.string.movie_list_not_found_text));
                        mLoadingView.setVisibility(View.GONE);
                        break;
                }
            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                getMostPopularMovieList();
                return true;
            case R.id.action_top_rating:
                getTopRatedMovieList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}