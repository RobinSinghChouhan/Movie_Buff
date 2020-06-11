package com.example.moviebuff;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key")String apiKey);
}
