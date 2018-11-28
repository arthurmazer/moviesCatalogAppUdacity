package com.mazerapp.moviecatalogapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "movies_favorite", foreignKeys = @ForeignKey(entity = MovieDetails.class,
        parentColumns = "idMovie",
        childColumns = "id",
        onDelete = CASCADE))
public class MovieFav {

    @PrimaryKey
    @NonNull
    private String id;
    private Boolean isFavorite;

    public MovieFav(String id, Boolean isFavorite) {
        this.id = id;
        this.isFavorite = isFavorite;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }


}
