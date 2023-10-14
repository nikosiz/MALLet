package com.example.mallet.client.configuration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //todo do poprawy jak postawimy gdzies to
    //todo no i https tez trzeba bd zrobic zeby stary nie zrobił ataku man in the middle czy innego chuja
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private RetrofitClient() {
    }

    public static Retrofit getRetrofitClient() {
        OkHttpClient httpClient = new OkHttpClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

    }
}
