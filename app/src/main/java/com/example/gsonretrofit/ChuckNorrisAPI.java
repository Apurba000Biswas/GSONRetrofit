package com.example.gsonretrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChuckNorrisAPI {

    @GET("/jokes/random")
    Call<String> randomJoke();

    @GET("/jokes/random/{num}")
    Call<String[]> allRandomJokes(@Path("num") int num);
}
