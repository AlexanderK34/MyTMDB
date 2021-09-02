package com.example.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.os.Bundle;

import com.example.mymovies.db.MovieDatabase;
import com.example.mymovies.modelMovies.MovieData;
import com.example.mymovies.modelMovies.MovieDataCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Favorites extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    MovieDatabase movieDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        initializeDB();
        initializeRecyclerView();
    }

    private void initialize() {

        MovieDataCollection.movieArrayList = new ArrayList<>();

        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, "moviedata").build();
    }

    private void initializeDB() {

        movieDatabase.getMovieDAO().getAll().subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<MovieData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<MovieData> movieData) {
                        MovieDataCollection.movieArrayList = (ArrayList<MovieData>) movieData;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewPersonal);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
        recyclerViewAdapter.notifyDataSetChanged();   // Refresh adapter
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeDB();
        recyclerViewAdapter.notifyDataSetChanged();
    }
}