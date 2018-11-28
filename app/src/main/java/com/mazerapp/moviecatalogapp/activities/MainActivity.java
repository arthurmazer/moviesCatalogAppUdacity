package com.mazerapp.moviecatalogapp.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.adapters.MovieCatalogAdapter;
import com.mazerapp.moviecatalogapp.models.Movie;
import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.utils.Constants;
import com.mazerapp.moviecatalogapp.utils.Util;
import com.mazerapp.moviecatalogapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_NO_CONNECTION;
import static com.mazerapp.moviecatalogapp.utils.Constants.ERROR_WITH_SERVICE;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvMovies;
    private MovieCatalogAdapter movieCatalogAdapter;
    private ArrayList<Movie.MovieInfo> listOfMovies;
    private Context context;
    private FrameLayout frameNoConnection;
    private SwipeRefreshLayout swipeLayout;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.subitem_most_popular));

        listOfMovies = new ArrayList<>();
        context = getApplicationContext();

        //inject
        rvMovies = findViewById(R.id.rv_movie_catalog);
        frameNoConnection = findViewById(R.id.frame_no_connection);
        swipeLayout = findViewById(R.id.swipe_layout);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        //initiate the adapter and set on click to items
        movieCatalogAdapter = new MovieCatalogAdapter(listOfMovies, movie -> {
            Intent it = new Intent(context, MovieDetailsActivity.class);
            it.putExtra(Constants.MOVIE_ID_EXTRA, movie.getId());
            it.putExtra(Constants.MOVIE_TITLE_EXTRA, movie.getTitle());
            it.putExtra(Constants.MOVIE_POSTER_PATH_EXTRA, movie.getPosterPath());
            startActivity(it);
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(mLayoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setAdapter(movieCatalogAdapter);

        swipeLayout.setOnRefreshListener(this);

        //load initial data
        getMostPopularMovieList();
    }


    private void getMostPopularMovieList() {
        swipeLayout.setRefreshing(true);
        mainViewModel.getMostPopularMovieList().observe(this, movie -> {
            if (movie != null){
                setTitle(getString(R.string.subitem_most_popular));
                dataLoadSuccess(movie.getMoviesInfoList());
            }else{
                dataLoadFail(ERROR_WITH_SERVICE);
            }
        });
    }

    private void getTopRatedMovieList() {
        swipeLayout.setRefreshing(true);

        mainViewModel.getTopRatedMovieList().observe(this, movie -> {
            if (movie != null){
                setTitle(getString(R.string.subitem_top_rating));
                dataLoadSuccess(movie.getMoviesInfoList());
            }else{
                dataLoadFail(ERROR_WITH_SERVICE);
            }
        });
    }

    private void getFavoritesMovies(){
        swipeLayout.setRefreshing(true);

        mainViewModel.getFavoriteMovies().observe(this, movie -> {
            if (movie != null){
                List<Movie.MovieInfo> movies = new ArrayList<>();

                for (MovieDetails movDetails : movie){
                    Movie movieObj = new Movie();
                    Movie.MovieInfo movieInfo = movieObj.new MovieInfo();

                    movieInfo.setPosterPath(movDetails.getPosterPath());
                    movieInfo.setTitle(movDetails.getTitle());
                    movieInfo.setId(movDetails.getIdMovie());

                    movies.add(movieInfo);
                }

                setTitle(getString(R.string.subitem_favorites));
                dataLoadSuccess(movies);
            }
        });
    }

    public void dataLoadSuccess(List<Movie.MovieInfo> movies){
        frameNoConnection.setVisibility(View.GONE);
        listOfMovies.clear();
        swipeLayout.setRefreshing(false);
        rvMovies.setVisibility(View.VISIBLE);
        listOfMovies.addAll(movies);
        movieCatalogAdapter.notifyDataSetChanged();
    }

    public void dataLoadFail(int code){
        swipeLayout.setRefreshing(false);
        switch (code){
            case ERROR_NO_CONNECTION:
                frameNoConnection.setVisibility(View.VISIBLE);
                rvMovies.setVisibility(View.GONE);
                break;
            case ERROR_WITH_SERVICE:
                Util.showDialog(context, context.getString(R.string.movie_list_not_found_titulo), context.getString(R.string.movie_list_not_found_text));
                break;
            default:
                Util.showDialog(context, context.getString(R.string.movie_list_not_found_titulo), context.getString(R.string.movie_list_not_found_text));
                break;
        }
    }

    public void doRefresh(){
        if (getTitle().equals(getString(R.string.subitem_most_popular)))
            getMostPopularMovieList();
        else if (getTitle().equals(getString(R.string.subitem_top_rating)))
            getTopRatedMovieList();
        else
            getFavoritesMovies();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public void onRefresh() {
        doRefresh();
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
            case R.id.action_favorites:
                getFavoritesMovies();
                return true;
            case R.id.action_refresh:
                doRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}