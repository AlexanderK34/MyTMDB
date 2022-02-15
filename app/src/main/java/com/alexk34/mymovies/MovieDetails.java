package com.alexk34.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.alexk34.mymovies.databinding.ActivityMovieDetailsBinding;
import com.alexk34.mymovies.modelVideo.Trailer;
import com.bumptech.glide.Glide;
import com.alexk34.mymovies.api.APIService;
import com.alexk34.mymovies.api.RetrofitClient;
import com.alexk34.mymovies.db.MovieDatabase;
import com.alexk34.mymovies.modelMovies.MovieData;
import com.alexk34.mymovies.modelMovieDetails.Details;
import com.alexk34.mymovies.modelPersons.Cast;
import com.alexk34.mymovies.modelPersons.CastDataCollection;
import com.alexk34.mymovies.modelPersons.Credits;
import com.alexk34.mymovies.modelPersons.Crew;
import com.alexk34.mymovies.modelVideo.Video;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class MovieDetails extends AppCompatActivity implements
        View.OnClickListener {

    MovieDatabase movieDatabase;
    MovieData movie;
    Details extraDetails;
    Credits credits;
    APIService apiService;
    ActivityMovieDetailsBinding binding;
    ArrayList<Trailer> videoTrailers;
    List<Crew> crew;
    int starOnId, starOffId;
    boolean inFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        initializeMovieDetailsAPI();
        initializeCreditsAPI();
        initializeViews();
        initializeDB();
        initializeTrailers();
    }

    private void initialize() {
        CastDataCollection.castArrayList = new ArrayList<>();

        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageFavorite.setOnClickListener(this);
        binding.imageUrl.setOnClickListener(this);
        binding.imageVideo.setOnClickListener(this);
        binding.imageCast.setOnClickListener(this);

        starOnId = (android.R.drawable.btn_star_big_on);
        starOffId = (android.R.drawable.btn_star_big_off);
    }

    private void initializeMovieDetailsAPI() {
        // Get Intent from ViewHolder
        movie = (MovieData) getIntent().getSerializableExtra("intentMovie");

        apiService = RetrofitClient.getClient(MainActivity.BASE_URL).create(APIService.class);
        apiService.getMovieDetails(movie.getId(), MainActivity.API_KEY, MainActivity.LANGUAGE)
                .enqueue(new Callback<Details>() {
                    @Override
                    public void onResponse(
                            Call<Details> call,
                            retrofit2.Response<Details> response) {
                        extraDetails = response.body();
                        addDetailsView();
                    }

                    @Override
                    public void onFailure(Call<Details> call, Throwable t) {
                    }
                });
    }

    private void initializeCreditsAPI() {
        apiService.getCredits(movie.getId(), MainActivity.API_KEY, MainActivity.LANGUAGE)
                .enqueue(new Callback<Credits>() {
                    @Override
                    public void onResponse(
                            Call<Credits> call,
                            retrofit2.Response<Credits> response) {
                        credits = response.body();
                        addPersons();
                    }

                    @Override
                    public void onFailure(Call<Credits> call, Throwable t) {
                    }
                });
    }

    private void initializeViews() {

        String urlMovie = movie.getOriginalPosterUrl();
        String textTitle = movie.getTitle();
        String textOverView = movie.getOverview();
        String textRelease = "Release date: " + movie.getReleaseDate();
        String textVote = movie.getVoteAverage() + " (" + movie.getVoteCount() + ")";

        // Set data to imageView and textView
        Glide.with(this)
                .load(urlMovie)
                .into(binding.imagePoster);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.textViewOverView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        binding.textViewTitle.setText(textTitle);
        binding.textViewOverView.setText(textOverView);
        binding.textViewRelease.setText(textRelease);
        binding.textViewVote.setText(textVote);
    }

    private void initializeDB() {
        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, "moviedata").build();

        movieDatabase.getMovieDAO().getById(movie.getId()).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new SingleObserver<MovieData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @SuppressLint("ResourceType")
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                        binding.imageFavorite.setImageResource(starOnId);
                        inFavorites = true;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.imageFavorite.setImageResource(starOffId);
                        inFavorites = false;
                    }
                });
    }

    private void initializeTrailers() {
        apiService.getVideo(movie.getId(), MainActivity.API_KEY, MainActivity.LANGUAGE).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, retrofit2.Response<Video> response) {
                Video video = response.body();
                videoTrailers = (ArrayList<Trailer>) video.getResults();
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageFavorite:
                addOrRemoveMovieFromFavorite();
                break;
            case R.id.imageUrl:
                goToHomePage();
                break;
            case R.id.imageVideo:
                goToVideoPage();
                break;
            case R.id.imageCast:
                goToCastPage();
                break;
        }
    }

    private void addOrRemoveMovieFromFavorite() {
        if (inFavorites) {
            deleteMovieFromDB();
        } else {
            addMovieToDB();
        }
        inFavorites = !inFavorites;
    }

    private void addMovieToDB() {
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
        binding.imageFavorite.setImageResource(starOnId);
        movieDatabase.getMovieDAO().insert(movie).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private void deleteMovieFromDB() {
        Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
        binding.imageFavorite.setImageResource(starOffId);
        movieDatabase.getMovieDAO().delete(movie).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private void goToHomePage() {
        try {
            String Url = extraDetails.getHomepage();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "No home page", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToVideoPage() {
        if (videoTrailers.size() < 1) {
            Toast.makeText(this, "No trailers", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("bundleTrailers", videoTrailers);

            Intent intent = new Intent(this, TrailersActivity.class);
            intent.putExtra("intentTrailers", bundle);
            startActivity(intent);
        }
    }

    private void addDetailsView() {

        StringBuilder genres = new StringBuilder();
        StringBuilder countries = new StringBuilder();

        for (int i = 0; i < extraDetails.getGenres().size(); i++) {
            genres.append(extraDetails.getGenres().get(i).getName());
            if (i == extraDetails.getGenres().size() - 1) {
                genres.append(".");
            } else {
                genres.append(", ");
            }
        }

        for (int i = 0; i < extraDetails.getProductionCountries().size(); i++) {
            countries.append(extraDetails.getProductionCountries().get(i).getName());
            if (i == extraDetails.getProductionCountries().size() - 1) {
                countries.append(".");
            } else {
                countries.append(", ");
            }
        }

        int runtimeHours = extraDetails.getRuntime() / 60;
        int runtimeMinutes = extraDetails.getRuntime() % 60;
        if (runtimeHours == 0) {
            binding.textRuntime.setText("Runtime: " + runtimeMinutes + "min");
        } else {
            binding.textRuntime.setText("Runtime: " + runtimeHours + "h " + runtimeMinutes + "m");
        }

        StringBuilder sbCompanies = new StringBuilder();
        sbCompanies.append("----------\n");
        for (int i = 0; i < extraDetails.getProductionCompanies().size(); i++) {
            sbCompanies.append(extraDetails.getProductionCompanies().get(i).getName()).append("\n");
        }
        sbCompanies.append("----------");

        NumberFormat formatter = new DecimalFormat("###,###,###,###");
        binding.textTagLine.setText(extraDetails.getTagline());
        binding.textGenres.setText("Genre: " + genres);
        binding.textOriginalTitle.setText("Original Title: " + extraDetails.getOriginalTitle());
        binding.textCountry.setText("Production country: " + countries);
        binding.textBudget.setText("Budget: $" + formatter.format(extraDetails.getBudget()));
        binding.textRevenue.setText("Revenue: $" + formatter.format(extraDetails.getRevenue()));
        binding.textProductionCompanies.setText(sbCompanies);
    }

    private void addPersons() {

        CastDataCollection.castArrayList = (ArrayList<Cast>) credits.getCast();
        crew = credits.getCrew();

        ArrayList<String> directors = new ArrayList<>();
        ArrayList<String> producers = new ArrayList<>();
        ArrayList<String> writers = new ArrayList<>();

        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getJob().equals("Director")) {
                directors.add(crew.get(i).getName());
            }
            if (crew.get(i).getJob().equals("Producer")) {
                producers.add(crew.get(i).getName());
            }
            if (crew.get(i).getDepartment().equals("Writing")) {
                writers.add(crew.get(i).getName());
            }
        }

        StringBuilder sbDirectors = new StringBuilder();
        StringBuilder sbProducers = new StringBuilder();
        StringBuilder sbWriters = new StringBuilder();

        for (int i = 0; i < directors.size(); i++) {
            sbDirectors.append(directors.get(i));
            if (i != directors.size() - 1) {
                sbDirectors.append("\n");
            }
        }

        for (int i = 0; i < producers.size(); i++) {
            sbProducers.append(producers.get(i));
            if (i != producers.size() - 1) {
                sbProducers.append("\n");
            }
        }

        for (int i = 0; i < writers.size(); i++) {
            sbWriters.append(writers.get(i));
            if (i != writers.size() - 1) {
                sbWriters.append("\n");
            }
        }

        binding.textDirector.setText("\nDirected by\n" + sbDirectors);
        binding.textProducer.setText("\nProduced by\n" + sbProducers);
        binding.textWriting.setText("\nWritten by\n" + sbWriters);
    }

    private void goToCastPage() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleCast", CastDataCollection.castArrayList);

        Intent intent = new Intent(this, CastActivity.class);
        intent.putExtra("intentCast", bundle);
        startActivity(intent);
    }
}
