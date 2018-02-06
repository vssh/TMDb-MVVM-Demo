package com.vssh.tmdbdemo.connector;

import com.vssh.tmdbdemo.BuildConfig;
import com.vssh.tmdbdemo.models.ItemList;
import com.vssh.tmdbdemo.models.MovieItem;
import com.vssh.tmdbdemo.models.TvItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API interface to TMDb for use with Retrofit.
 */

public interface TmdbApi {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_POSTER_SMALL_URL = "https://image.tmdb.org/t/p/w92/";
    String IMAGE_POSTER_LARGE_URL = "https://image.tmdb.org/t/p/w185/";
    String IMAGE_BACKDROP_URL = "https://image.tmdb.org/t/p/w500/";

    // API key is set in gradle.properties file to avoid exposing it
    String API_KEY = BuildConfig.TMDB_API_KEY;

    /**
     * Get movies by category. Can be useful in future to extend the app
     * @param category movie/tv list category ex. popular
     * @return Call
     */
    @GET("movie/{category}?api_key="+API_KEY)
    Call<ItemList<MovieItem>> getItems(
            @Path("category") String category
    );

    /**
     * Get results for movie query
     * @param query search term
     * @return Call
     */
    @GET("search/movie?api_key="+API_KEY)
    Call<ItemList<MovieItem>> getMovieSearchItems(
            @Query("query") String query
    );

    /**
     * Get results for tv query
     * @param query search term
     * @return Call
     */
    @GET("search/tv?api_key="+API_KEY)
    Call<ItemList<TvItem>> getTvSearchItems(
            @Query("query") String query
    );
}
