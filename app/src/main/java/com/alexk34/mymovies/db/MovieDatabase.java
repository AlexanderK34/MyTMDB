package com.alexk34.mymovies.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.alexk34.mymovies.modelMovies.MovieData;

@Database(entities = {MovieData.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDAO getMovieDAO();
}
