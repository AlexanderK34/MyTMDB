package com.example.mymovies.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymovies.modelMovies.MovieData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM moviedata")
    Single<List<MovieData>> getAll();

    @Query("SELECT * FROM moviedata WHERE id = :id")
    Single<MovieData> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(MovieData movieData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<MovieData> movieData);

    @Update
    Completable update(MovieData movieData);

    @Delete
    Completable delete(MovieData movieData);
}
