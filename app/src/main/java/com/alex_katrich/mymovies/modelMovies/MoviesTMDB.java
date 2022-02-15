package com.alex_katrich.mymovies.modelMovies;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MoviesTMDB implements Serializable {

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private ArrayList<MovieData> movieData = new ArrayList<>();

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("total_results")
    private Integer totalResults;

    public ArrayList<MovieData> getResults() {
        return movieData;
    }

    public void setResults(ArrayList<MovieData> movieData) {
        this.movieData = movieData;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public String toString() {
        return "MoviesTMDB{" +
                "page=" + page +
                ", movieData=" + movieData +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                '}';
    }
}
