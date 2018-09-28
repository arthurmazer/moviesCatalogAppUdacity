package com.mazerapp.moviecatalogapp.models.retrofit;

import com.google.gson.annotations.SerializedName;

public class MovieDetails {

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
}
