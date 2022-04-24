package com.alexk34.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.alexk34.mymovies.databinding.ActivityMainBinding;
import com.alexk34.mymovies.modelMovies.MovieDataCollection;
import com.alexk34.mymovies.modelMovies.MoviesTMDB;
import com.alexk34.mymovies.api.APIService;
import com.alexk34.mymovies.api.RetrofitClient;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        View.OnClickListener {

    public static final String API_KEY = "d241f8c40debbe6d6fb779f086d60860";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static String LANGUAGE = "en-US";
    public final int TOP_RATED = 1;
    public final int POPULAR = 2;
    public final int NOW_PLAYING = 3;
    public final int UPCOMING = 4;

    String searchQuery;
    int currentPage, totalPages, currentList;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    APIService apiService;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        initializeRecyclerView();
        initializeSearchView();
        initializeMoviesList();
    }

    private void initialize() {
        currentPage = 1;
        currentList = POPULAR;
        MovieDataCollection.movieArrayList = new ArrayList<>();
        apiService = RetrofitClient.getClient(BASE_URL).create(APIService.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewLanguage.setOnClickListener(this);
        binding.editPageNumber.setOnClickListener(this);
        binding.imageFavorites.setOnClickListener(this);
        binding.imageFirstPage.setOnClickListener(this);
        binding.imagePreviousPage.setOnClickListener(this);
        binding.imageNextPage.setOnClickListener(this);
        binding.imageLastPage.setOnClickListener(this);
        binding.rbNowPlaying.setOnClickListener(this);
        binding.rbPopular.setOnClickListener(this);
        binding.rbTopRated.setOnClickListener(this);
        binding.rbUpcoming.setOnClickListener(this);
        binding.rbPopular.setChecked(true);
    }

    private void initializeRecyclerView() {
        recyclerView = binding.recyclerViewMain;
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
    }

    private void initializeSearchView() {
        binding.searchView.setOnQueryTextListener(this);
    }

    private void initializeMoviesList() {
        if (searchQuery != null) {
            getMoviesSearch(searchQuery);
        } else if (currentList == TOP_RATED) {
            getMoviesTopRated();
        } else if (currentList == POPULAR) {
            getMoviesPopular();
        } else if (currentList == NOW_PLAYING) {
            getMoviesNowPlaying();
        } else if (currentList == UPCOMING) {
            getMoviesUpcoming();
        }
    }

    public void getMoviesPopular() {
        apiService.getPopularList(API_KEY, LANGUAGE, currentPage).enqueue(new Callback<MoviesTMDB>() {
            @Override
            public void onResponse(Call<MoviesTMDB> call, retrofit2.Response<MoviesTMDB> response) {
                getNewMovieList(response);
            }

            @Override
            public void onFailure(Call<MoviesTMDB> call, Throwable t) {
            }
        });
    }

    public void getMoviesNowPlaying() {
        apiService.getNowPlayingList(API_KEY, LANGUAGE, currentPage).enqueue(new Callback<MoviesTMDB>() {
            @Override
            public void onResponse(Call<MoviesTMDB> call, retrofit2.Response<MoviesTMDB> response) {
                getNewMovieList(response);
            }

            @Override
            public void onFailure(Call<MoviesTMDB> call, Throwable t) {
            }
        });
    }

    private void getMoviesUpcoming() {
        apiService.getUpcomingList(API_KEY, LANGUAGE, currentPage).enqueue(new Callback<MoviesTMDB>() {
            @Override
            public void onResponse(Call<MoviesTMDB> call, retrofit2.Response<MoviesTMDB> response) {
                getNewMovieList(response);
            }

            @Override
            public void onFailure(Call<MoviesTMDB> call, Throwable t) {
            }
        });
    }

    public void getMoviesTopRated() {
        apiService.getTopRatedList(API_KEY, LANGUAGE, currentPage).enqueue(new Callback<MoviesTMDB>() {
            @Override
            public void onResponse(Call<MoviesTMDB> call, retrofit2.Response<MoviesTMDB> response) {
                getNewMovieList(response);
            }

            @Override
            public void onFailure(Call<MoviesTMDB> call, Throwable t) {
            }
        });
    }

    public void getMoviesSearch(String query) {
        apiService.getSearchList(query, API_KEY, LANGUAGE, currentPage).enqueue(new Callback<MoviesTMDB>() {
            @Override
            public void onResponse(Call<MoviesTMDB> call, retrofit2.Response<MoviesTMDB> response) {
                getNewMovieList(response);
            }

            @Override
            public void onFailure(Call<MoviesTMDB> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        MovieDataCollection.movieArrayList.clear();        // Clear gallery
        currentPage = 1;
        searchQuery = query;
        getMoviesSearch(query);
        binding.searchView.setQuery(null, false);    // Clear the text in SearchView
        binding.searchView.clearFocus();
        binding.rbPopular.setChecked(false);
        binding.rbTopRated.setChecked(false);
        binding.rbNowPlaying.setChecked(false);
        binding.rbUpcoming.setChecked(false);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageFavorites:
                goToFavoriteList();
                break;
            case R.id.imageViewLanguage:
                goToChangeLanguage();
                break;
            case R.id.imageFirstPage:
                getFirstPage();
                break;
            case R.id.imagePreviousPage:
                getPreviousPage();
                break;
            case R.id.imageNextPage:
                getNextPage();
                break;
            case R.id.imageLastPage:
                getLastPage();
                break;
            case R.id.editPageNumber:
                getPageNumber(view);
                break;
            case R.id.rbTopRated:
                currentList = TOP_RATED;
                currentPage = 1;
                searchQuery = null;
                getMoviesTopRated();
                break;
            case R.id.rbPopular:
                currentList = POPULAR;
                currentPage = 1;
                searchQuery = null;
                getMoviesPopular();
                break;
            case R.id.rbNowPlaying:
                currentList = NOW_PLAYING;
                currentPage = 1;
                searchQuery = null;
                getMoviesNowPlaying();
                break;
            case R.id.rbUpcoming:
                currentList = UPCOMING;
                currentPage = 1;
                searchQuery = null;
                getMoviesUpcoming();
                break;
        }
    }

    private void getNewMovieList(retrofit2.Response<MoviesTMDB> response) {
        MovieDataCollection.movieArrayList = response.body().getResults();
        totalPages = response.body().getTotalPages();
        String hint;
        if (currentList == POPULAR) {   // Fix wrong API index for view Popular Max
            hint = currentPage + " of 500";
        } else {
            hint = currentPage + " of " + response.body().getTotalPages();
        }
        binding.editPageNumber.setHint(hint);
        recyclerViewAdapter.notifyDataSetChanged();   // Refresh adapter
    }

    private void goToFavoriteList() {
        Intent myIntent = new Intent(this, Favorites.class);
        startActivity(myIntent);
    }

    private void getFirstPage() {
        currentPage = 1;
        initializeMoviesList();
    }

    private void getPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            initializeMoviesList();
        }
    }

    private void getNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            initializeMoviesList();
        }
    }

    private void getLastPage() {
        currentPage = totalPages;
        initializeMoviesList();
    }

    private void getPageNumber(View view) {
        String newPageString = binding.editPageNumber.getText().toString();
        try {
            int newPageInt = Integer.parseInt(newPageString);
            if (newPageInt < 1) {
                currentPage = 1;
            } else currentPage = Math.min(newPageInt, totalPages);
            if (currentList == POPULAR && currentPage > 500) { // Popular List = 500 pages max
                currentPage = 500;                       // But API has wrong max index
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        binding.editPageNumber.setText(null);
        hideKeyboard(view);
        initializeMoviesList();
    }

    private void goToChangeLanguage() {
        if (LANGUAGE.equals("en-US")) {
            LANGUAGE = "ru-RU";
            binding.imageViewLanguage.setImageResource(R.drawable.russia_flag);
        } else {
            LANGUAGE = "en-US";
            binding.imageViewLanguage.setImageResource(R.drawable.gb_flag);
        }
        initializeMoviesList();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        binding.editPageNumber.clearFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LANGUAGE.equals("en-US")) {
            binding.imageViewLanguage.setImageResource(R.drawable.gb_flag);
        } else {
            binding.imageViewLanguage.setImageResource(R.drawable.russia_flag);
        }
        initializeMoviesList();
    }
}