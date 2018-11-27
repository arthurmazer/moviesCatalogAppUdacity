package com.mazerapp.moviecatalogapp.repositories;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mazerapp.moviecatalogapp.interfaces.room.MovieDao;
import com.mazerapp.moviecatalogapp.models.MovieDetails;
import com.mazerapp.moviecatalogapp.models.MovieFav;
import com.mazerapp.moviecatalogapp.utils.Constants;

import static com.mazerapp.moviecatalogapp.utils.Constants.DB_VERSION;

@Database(entities = {MovieDetails.class, MovieFav.class}, version = DB_VERSION)
public abstract class DbManager extends RoomDatabase {

    public static DbManager instance;
    public abstract MovieDao movieDao();

    public static DbManager getInstance(Context ctx){
        if ( instance == null ){
            instance = Room.databaseBuilder(ctx, DbManager.class, Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

}
