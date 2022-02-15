package com.alex_katrich.mymovies;

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
import com.example.mymovies.R;
import com.alex_katrich.mymovies.modelPersons.Cast;
import com.alex_katrich.mymovies.modelPersons.CastDataCollection;

public class RecyclerViewAdapterCast extends
        RecyclerView.Adapter<RecyclerViewAdapterCast.RecyclerView_ViewHolder> {

    Context context;

    public RecyclerViewAdapterCast(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_list_cast,
                parent,
                false);
        return new RecyclerView_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterCast.RecyclerView_ViewHolder holder, int position) {

        Cast cast = CastDataCollection.castArrayList.get(position);
        if (cast.getProfilePath() == null) {
            holder.imageViewCast.setImageResource(R.drawable.person_white);
        } else {
            Glide.with(context)
                    .load(cast.getOriginalPosterUrl())
                    .into(holder.imageViewCast);
        }
        holder.textViewCast.setText(cast.getName());
        holder.textViewCharacter.setText(cast.getCharacter());
        holder.textSpace.setText("                                                                  " +
                "                                                                                   " +
                "                                                                                    ");
        holder.cellViewCast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CastDetailsActivity.class);
                intent.putExtra("intentPerson", cast.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CastDataCollection.castArrayList.size();
    }

    public static class RecyclerView_ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCast;
        TextView textViewCharacter;
        TextView textSpace;
        ImageView imageViewCast;
        LinearLayout cellViewCast;

        public RecyclerView_ViewHolder(View itemView) {
            super(itemView);
            textViewCast = itemView.findViewById(R.id.textViewCast);
            textViewCharacter = itemView.findViewById(R.id.textViewCharacter);
            textSpace = itemView.findViewById(R.id.textSpace);
            imageViewCast = itemView.findViewById(R.id.imageViewCast);
            cellViewCast = itemView.findViewById(R.id.cellViewCast);
        }
    }
}
