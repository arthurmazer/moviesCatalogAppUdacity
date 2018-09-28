package com.mazerapp.moviecatalogapp.models.retrofit;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arthur on 09/09/2018.
 */

public class Movie {

    @SerializedName("page")
    private int pageNumber;
    @SerializedName("total_results")
    private long totalResults;
    @SerializedName("total_pages")
    private long totalPages;
    @SerializedName("results")
    private List<MovieInfo> moviesInfoList;


    public List<MovieInfo> getMoviesInfoList() {
        return moviesInfoList;
    }


    public class MovieInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("vote_count")
        private long voteCount;
        @SerializedName("vote_average")
        private float voteAverage;
        @SerializedName("title")
        private String title;
        @SerializedName("popularity")
        private float popularity;
        @SerializedName("poster_path")
        private String posterPath;
        @SerializedName("original_language")
        private String originalLanguage;
        @SerializedName("original_title")
        private String originalTitle;
        @SerializedName("backdrop_path")
        private String backdropPath;
        @SerializedName("adult")
        private Boolean adult;
        @SerializedName("genre_ids")
        private List<Integer> genreIdList;
        @SerializedName("overview")
        private String overview;
        @SerializedName("release_date")
        private String releaseDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getPosterPath() {
            return posterPath;
        }


    }

}
