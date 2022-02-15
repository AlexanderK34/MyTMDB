package com.alexk34.mymovies.modelMovieDetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpokenLanguage implements Serializable {

    @SerializedName("english_name")
    private String englishName;

    @SerializedName("iso_639_1")
    private String iso6391;

    @SerializedName("name")
    private String name;

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SpokenLanguage{" +
                "englishName='" + englishName + '\'' +
                ", iso6391='" + iso6391 + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
