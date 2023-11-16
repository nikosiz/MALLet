package com.example.mallet.backend.client.set.boundary;

import androidx.annotation.NonNull;

import com.agh.api.Language;
import com.agh.api.SetBasicDTO;
import com.agh.api.SetDetailDTO;
import com.example.mallet.backend.client.configuration.RetrofitClient;
import com.example.mallet.backend.client.set.control.SetService;

import java.util.Set;

import retrofit2.Callback;

public class SetServiceImpl {

    private final SetService setService;

    public SetServiceImpl(String credential) {
        this.setService = RetrofitClient.getRetrofitClient(credential)
                .create(SetService.class);
    }

    public void getSet(@NonNull Long id,
                       Callback<SetDetailDTO> callback) {
        setService.getSet(id)
                .enqueue(callback);
    }

    public void getBasicSet(@NonNull String topic,
                            Callback<SetBasicDTO> callback) {
        setService.getBasicSet(topic)
                .enqueue(callback);
    }

    public void getBasicSet(@NonNull Set<Long> ids,
                            Callback<SetBasicDTO> callback) {
        setService.getBasicSet(ids)
                .enqueue(callback);
    }

    public void getBasicSet(long startPosition,
                            long limit,
                            @NonNull Language language,
                            Callback<SetBasicDTO> callback) {
        setService.getBasicSet(startPosition, limit, language)
                .enqueue(callback);
    }

    public void getBasicSet(long startPosition,
                            long limit,
                            boolean predefined,
                            Callback<SetBasicDTO> callback) {
        setService.getBasicSet(startPosition, limit, predefined)
                .enqueue(callback);
    }

 }