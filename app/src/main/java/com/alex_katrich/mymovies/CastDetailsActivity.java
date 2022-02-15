package com.alex_katrich.mymovies;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.mymovies.R;
import com.alex_katrich.mymovies.api.APIService;
import com.alex_katrich.mymovies.api.RetrofitClient;
import com.example.mymovies.databinding.ActivityCastDetailsBinding;
import com.alex_katrich.mymovies.modelPersonDetails.PersonDetails;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class CastDetailsActivity extends AppCompatActivity {

    APIService apiService;
    ActivityCastDetailsBinding binding;
    int personId;
    PersonDetails personDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        initializeApi();
    }

    private void initialize() {
        personId = (Integer) getIntent().getSerializableExtra("intentPerson");
        binding = ActivityCastDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initializeApi() {

        apiService = RetrofitClient.getClient(MainActivity.BASE_URL).create(APIService.class);
        apiService.getPersonDetails(personId, MainActivity.API_KEY, MainActivity.LANGUAGE)
                .enqueue(new Callback<PersonDetails>() {
                    @Override
                    public void onResponse(
                            Call<PersonDetails> call,
                            retrofit2.Response<PersonDetails> response) {
                        personDetails = response.body();
                        addPersonDetailsView();
                    }

                    @Override
                    public void onFailure(Call<PersonDetails> call, Throwable t) {
                    }
                });
    }

    private void addPersonDetailsView() {

        Glide.with(this)
                .load(personDetails.getOriginalPosterUrl())
                .into(binding.imageCastPoster);

        // Set Name
        binding.textViewName.setText(personDetails.getName());

        // Set Also Known As
        StringBuilder otherNames = new StringBuilder();
        for (int i = 0; i < personDetails.getAlsoKnownAs().size(); i++) {
            otherNames.append(personDetails.getAlsoKnownAs().get(i));
            if (i != personDetails.getAlsoKnownAs().size() - 1) {
                otherNames.append("\n");
            }
        }
        binding.textAlsoKnownAs.setText(otherNames);

        // Set BirthDay
        String personDate;
        if (personDetails.getBirthday() == null) {
            personDate = "Born:";
        } else if (personDetails.getDeathday() == null) {
            personDate = "Born: " + personDetails.getBirthday();
        } else {
            personDate = "Born: " + personDetails.getBirthday() + "\nDied: " + personDetails.getDeathday();
        }
        binding.textBirthday.setText(personDate);

        // Set Place of Birth
        if (personDetails.getPlaceOfBirth() == null) {
            binding.textViewPlaceOfBirth.setText("Place of Birth:");
        } else {
            binding.textViewPlaceOfBirth.setText("Place of Birth: " + personDetails.getPlaceOfBirth());
        }

        // Set Biography
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.textBiography.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        binding.textBiography.setText(personDetails.getBiography());
    }
}