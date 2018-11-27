package com.mazerapp.moviecatalogapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies_details")
public class MovieDetails {

    @PrimaryKey
    @NonNull
    private String idMovie;

    @SerializedName("title")
    private String title;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;



    public String getTitle() {
        return title;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @NonNull
    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(@NonNull String idMovie) {
        this.idMovie = idMovie;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
