package com.example.gym_planner;

import static com.example.gym_planner.RapidApi.API_KEY;
import static com.example.gym_planner.RapidApi.BASE_URL;

import android.content.Context;
import android.util.Log;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationRepository {

    private static final String TAG = "ApplicationRepository";

    private static ApplicationRepository applicationRepository;
    private final RapidApi rapidApi;

    public ApplicationRepository(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rapidApi = retrofit.create(RapidApi.class);
    }

    public static ApplicationRepository getInstance(Context context) {
        if (applicationRepository == null) {
            applicationRepository = new ApplicationRepository(context);
        }
        Log.i(TAG, "getInstance: Instance returned");
        return applicationRepository;
    }

    public void checkBMI(double weight, double height, Callback<Bmijson> callback) {
        rapidApi.checkBMI(weight, height, API_KEY).enqueue(callback);
    }
}
