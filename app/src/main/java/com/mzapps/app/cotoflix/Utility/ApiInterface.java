package com.mzapps.app.cotoflix.Utility;

import com.mzapps.app.cotoflix.Model.Actor;
import com.mzapps.app.cotoflix.Model.ActorMovies;
import com.mzapps.app.cotoflix.Model.ActorResponse;
import com.mzapps.app.cotoflix.Model.Credits;
import com.mzapps.app.cotoflix.Model.GenreResponse;
import com.mzapps.app.cotoflix.Model.Images_tmdb;
import com.mzapps.app.cotoflix.Model.MovieDetails;
import com.mzapps.app.cotoflix.Model.MoviesResponse;
import com.mzapps.app.cotoflix.Model.SeasonDetails;
import com.mzapps.app.cotoflix.Model.Slider;
import com.mzapps.app.cotoflix.Model.TVDetails;
import com.mzapps.app.cotoflix.Model.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET("movie/upcoming")
    Call<MoviesResponse> getupcomingMovies(@Query("api_key") String apiKey,@Query("page") int pageIndex);

    @GET("movie/popular")
    Call<MoviesResponse> getpopularMovies(@Query("api_key") String apiKey,@Query("page") int pageIndex);


    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey,@Query("page") int pageIndex);


    @GET("tv/top_rated")
    Call<MoviesResponse> getTopRatedTV(@Query("api_key") String apiKey,@Query("page") int pageIndex);

    @GET("tv/popular")
    Call<MoviesResponse> getpopularTV(@Query("api_key") String apiKey,@Query("page") int pageIndex);

    @GET("person/popular")
    Call<ActorResponse> getpopularActors(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(@Query("api_key") String apiKey);





    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{type}/{id}/videos")
    Call<TrailerResponse> gettrailers(@Path("type") String type,@Path("id") int id, @Query("api_key") String apiKey);


    @GET("movie/{id}/credits")
    Call<Credits> getMovieCredits(@Path("id") int id, @Query("api_key") String apiKey);



    @GET("search/multi")
    Call<MoviesResponse> getMovieSearch(@Query("query") String query, @Query("api_key") String apiKey);


    @GET("tv/{id}")
    Call<TVDetails> getTVDetails(@Path("id") int id, @Query("api_key") String apiKey);
    @GET("tv/{id}/credits")
    Call<Credits> getTVCredits(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("tv/{id}/season/{number}")
    Call<SeasonDetails> getepisodes(@Path("id") int id, @Path("number") int number, @Query("api_key") String apiKey);

    @GET("{type}/{id}/images")
    Call<Images_tmdb> getImages(@Path("type") String type,@Path("id") int id, @Query("api_key") String apiKey);


    @GET("person/{id}")
    Call<Actor> getActorDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<ActorMovies> getActorMovies(@Path("id") int id, @Query("api_key") String apiKey);
    @GET
    Call<List<Slider>> getSlider(@Url String url);


}
