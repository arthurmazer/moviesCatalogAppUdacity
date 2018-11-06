package com.mazerapp.moviecatalogapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.models.retrofit.Movie;
import com.mazerapp.moviecatalogapp.models.retrofit.MovieTrailers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur 06/11/2018.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder>{

    private List<MovieTrailers.TrailerInfo> trailers;
    private final OnClickItem clickListener;

    public MovieTrailersAdapter(OnClickItem clickListener){
        this.trailers = new ArrayList<>();
        this.clickListener = clickListener;
    }

    public void setListTrailers(List<MovieTrailers.TrailerInfo> trailers){
        this.trailers.clear();
        this.trailers.addAll(trailers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_trailer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MovieTrailers.TrailerInfo trailer = this.trailers.get(position);
        holder.trailerTitle.setText(trailer.getName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onTrailerClicked(trailer);
            }
        });
    }


    @Override
    public int getItemCount() {
        return trailers == null ? 0 : trailers.size();
    }

    static class ViewHolder  extends RecyclerView.ViewHolder {

        private TextView trailerTitle;
        private RelativeLayout parentLayout;

        private ViewHolder(View itemView) {
            super(itemView);
            this.trailerTitle = itemView.findViewById(R.id.tv_trailer_id);
            this.parentLayout = itemView.findViewById(R.id.parent_layout_trailer);
        }

    }

    public interface OnClickItem{
        void onTrailerClicked(MovieTrailers.TrailerInfo trailerInfo);
    }
}
