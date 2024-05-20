package com.example.mynextgameonsteam;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.steampowered.com/";
    private static final String STORE_URL = "https://store.steampowered.com/";
    private static Retrofit retrofit = null;
    private static Retrofit storeRetrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getStoreClient() {
        if (storeRetrofit == null) {
            storeRetrofit = new Retrofit.Builder()
                    .baseUrl(STORE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return storeRetrofit;
    }
}
