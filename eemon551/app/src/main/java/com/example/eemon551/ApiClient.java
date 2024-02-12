package com.example.eemon551;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// ApiClient.java
public class ApiClient {
    private static final String BASE_URL = "https://eemon-551.onrender.com/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
