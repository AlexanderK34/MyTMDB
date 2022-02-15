
package com.alex_katrich.mymovies.modelVideo;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Video implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("results")
    private List<Trailer> trailers = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return trailers;
    }

    public void setResults(List<Trailer> trailers) {
        this.trailers = trailers;
    }

}
