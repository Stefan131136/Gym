package com.example.gym_planner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RapidApi {

    public static final String BASE_URL = "https://body-mass-index-bmi-calculator.p.rapidapi.com/";

    static final String API_KEY = "724d50db92mshaac0650aed2e9b3p17160fjsnb4d6dfc27716";


    @GET("metric")
    Call<Bmijson> checkBMI(
            @Query("weight") double weight,
            @Query("height") double height,
            @Header("x-rapidapi-key") String apiKey);
}
