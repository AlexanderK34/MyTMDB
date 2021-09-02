package com.example.mymovies.api;

import com.example.mymovies.modelMovies.MoviesTMDB;
import com.example.mymovies.modelMovieDetails.Details;
import com.example.mymovies.modelPersonDetails.PersonDetails;
import com.example.mymovies.modelPersons.Credits;
import com.example.mymovies.modelVideo.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie/popular")
    Call<MoviesTMDB> getPopularList(
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE,
            @Query("page") int PAGE
    );

    @GET("movie/top_rated")
    Call<MoviesTMDB> getTopRatedList(
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE,
            @Query("page") int PAGE
    );

    @GET("movie/now_playing")
    Call<MoviesTMDB> getNowPlayingList(
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE,
            @Query("page") int PAGE
    );

    @GET("movie/upcoming")
    Call<MoviesTMDB> getUpcomingList(
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE,
            @Query("page") int PAGE
    );

    @GET("search/movie")
    Call<MoviesTMDB> getSearchList(
            @Query("query") String query,
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE,
            @Query("page") int PAGE
    );

    @GET("movie/{id}")
    Call<Details> getMovieDetails(
            @Path("id") int id,
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE
    );

    @GET("movie/{id}/videos")
    Call<Video> getVideo(
            @Path("id") int id,
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE
    );

    @GET("movie/{id}/credits")
    Call<Credits> getCredits(
            @Path("id") int id,
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE
    );

    @GET("person/{person_id}")
    Call<PersonDetails> getPersonDetails(
            @Path("person_id") int id,
            @Query("api_key") String API_KEY,
            @Query("language") String LANGUAGE
    );
}
