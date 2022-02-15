package com.alex_katrich.mymovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex_katrich.mymovies.modelVideo.Trailer;
import com.example.mymovies.R;
import com.alex_katrich.mymovies.modelVideo.TrailerDataCollection;

public class RecyclerViewAdapterTrailers extends
        RecyclerView.Adapter<RecyclerViewAdapterTrailers.RecyclerView_ViewHolder> {

    Context context;

    public RecyclerViewAdapterTrailers(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_list_trailers,
                parent,
                false);
        return new RecyclerView_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterTrailers.RecyclerView_ViewHolder holder, int position) {

        Trailer trailer = TrailerDataCollection.trailerArrayList.get(position);
        holder.textViewTrailer.setText(trailer.getName());
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setAppCacheEnabled(true);
        holder.webView.getSettings().setBuiltInZoomControls(true);
        holder.webView.getSettings().setSaveFormData(true);
        holder.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        holder.webView.loadUrl(trailer.getYouTubeLink());
        holder.webView.setWebChromeClient(new WebChromeClient());
        holder.imageViewYouTube.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    String Url = trailer.getYouTubeLink();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                    context.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return TrailerDataCollection.trailerArrayList.size();
    }

    public static class RecyclerView_ViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextView textViewTrailer;
        ImageView imageViewYouTube;

        public RecyclerView_ViewHolder(View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
            textViewTrailer = itemView.findViewById(R.id.textViewTrailer);
            imageViewYouTube = itemView.findViewById(R.id.imageViewYouTube);
        }
    }
}
