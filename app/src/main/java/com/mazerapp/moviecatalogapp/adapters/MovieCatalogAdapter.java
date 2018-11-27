package com.mazerapp.moviecatalogapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.models.Movie;
import com.mazerapp.moviecatalogapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arthur on 09/09/2018.
 */

public class MovieCatalogAdapter extends RecyclerView.Adapter<MovieCatalogAdapter.ViewHolder>{

    private ArrayList<Movie.MovieInfo> listOfMovies;
    private final OnClickItem clickListener;

    public MovieCatalogAdapter(ArrayList<Movie.MovieInfo> listOfMovies, OnClickItem clickListener){
        this.listOfMovies = listOfMovies;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public MovieCatalogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_movie_poster,parent,false);
        return new ViewHolder(view);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull MovieCatalogAdapter.ViewHolder holder, int position) {
        final Movie.MovieInfo movie = this.listOfMovies.get(position);

        Context context = holder.poster.getContext();


        String urlPoster = Constants.MOVIEES_DB_BASE_IMG_URL + Constants.MOVIES_DB_IMG_SIZES[2] + movie.getPosterPath();
        Picasso.with(context).load(urlPoster).into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onMovieClicked(movie);
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return listOfMovies == null ? 0 : listOfMovies.size();
    }

    static class ViewHolder  extends RecyclerView.ViewHolder {

        private ImageView poster;

        private ViewHolder(View itemView) {
            super(itemView);
            this.poster = itemView.findViewById(R.id.iv_movie_poster);

        }

    }

    public interface OnClickItem{
        void onMovieClicked(Movie.MovieInfo movie);
    }
}
