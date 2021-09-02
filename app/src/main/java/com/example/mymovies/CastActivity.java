package com.example.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.example.mymovies.modelPersons.Cast;
import com.example.mymovies.modelPersons.CastDataCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class CastActivity extends AppCompatActivity {

    RecyclerView recyclerViewCast;
    RecyclerViewAdapterCast recyclerViewAdapterCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        initializeRecyclerView();
    }

    private void initialize() {

        Bundle bundle = getIntent().getBundleExtra("intentCast");
        Serializable bundleCast = bundle.getSerializable("bundleCast");

        CastDataCollection.castArrayList = (ArrayList<Cast>) bundleCast;

    }

    private void initializeRecyclerView() {
        recyclerViewCast = findViewById(R.id.recyclerViewCast);
        recyclerViewAdapterCast = new RecyclerViewAdapterCast(this);
        recyclerViewCast.setAdapter(recyclerViewAdapterCast);
        recyclerViewCast.setLayoutManager(
                new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        recyclerViewAdapterCast.notifyDataSetChanged();   // Refresh adapter
    }
}