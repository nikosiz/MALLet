package com.example.mallet.backend.client.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //todo do poprawy jak postawimy gdzies to
    //todo no i https tez trzeba bd zrobic zeby stary nie zrobił ataku man in the middle czy innego chuja
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final Gson gson;

    static {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
            }
        }).create();
    }

    private RetrofitClient() {
    }

    public static Retrofit getRetrofitClient() {
        OkHttpClient httpClient = new OkHttpClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

    }
}
