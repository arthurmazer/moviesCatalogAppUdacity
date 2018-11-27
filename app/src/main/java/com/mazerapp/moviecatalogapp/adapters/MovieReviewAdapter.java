package com.mazerapp.moviecatalogapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mazerapp.moviecatalogapp.R;
import com.mazerapp.moviecatalogapp.models.MovieReviews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthur 06/11/2018.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder>{

    private List<MovieReviews.Reviews> reviews;

    public MovieReviewAdapter(){
        this.reviews = new ArrayList<>();
    }

    public void setListReviews(List<MovieReviews.Reviews> reviews){
        this.reviews.clear();
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_review,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MovieReviews.Reviews review = this.reviews.get(position);
        holder.reviewerUser.setText(review.getAuthor());
        holder.review.setText(review.getContent());
    }


    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    static class ViewHolder  extends RecyclerView.ViewHolder {

        private TextView reviewerUser;
        private TextView review;

        private ViewHolder(View itemView) {
            super(itemView);
            this.reviewerUser = itemView.findViewById(R.id.tv_reviewer_username);
            this.review = itemView.findViewById(R.id.tv_review);
        }

    }
}
