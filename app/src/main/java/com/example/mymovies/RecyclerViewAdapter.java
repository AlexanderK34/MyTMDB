package com.example.mymovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymovies.modelMovies.MovieDataCollection;
import com.example.mymovies.modelMovies.MovieData;

public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerView_ViewHolder> {

    Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_list_movies,
                parent,
                false);
        return new RecyclerView_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.RecyclerView_ViewHolder holder, int position) {

        MovieData movie = MovieDataCollection.movieArrayList.get(position);
        Glide.with(context)
                .load(movie.getOriginalPosterUrl())
                .into(holder.imageView);
        holder.textView.setText(movie.getTitle());
        holder.cellView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MovieDetails.class);
                intent.putExtra("intentMovie", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MovieDataCollection.movieArrayList.size();
    }

    public static class RecyclerView_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout cellView;

        public RecyclerView_ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewMainPoster);
            textView = itemView.findViewById(R.id.textViewMainTitle);
            cellView = itemView.findViewById(R.id.cellView);
        }
    }
}
