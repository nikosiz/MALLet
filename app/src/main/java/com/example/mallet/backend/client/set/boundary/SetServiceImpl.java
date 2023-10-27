package com.example.mallet.backend.client.set.boundary;

import com.example.mallet.backend.client.configuration.RetrofitClient;
import com.example.mallet.backend.client.set.control.SetService;

public class SetServiceImpl {

    private final SetService setService;

    public SetServiceImpl() {
        this.setService = RetrofitClient.getRetrofitClient()
                .create(SetService.class);
    }

    /*public void getSet(@NonNull Long id,
                       long termStartPosition,
                       long termLimit,
                       @NonNull Language language,
                       Callback<SetDetailDTO> callback) {
        setService.getSet(id, termStartPosition, termLimit, language)
                .enqueue(callback);
    }

    public void getBasicSet(@NonNull Set<Long> ids,
                            long startPosition,
                            long limit,
                            Callback<SetBasicDTO> callback) {
        setService.getBasicSet(ids, startPosition, limit)
                .enqueue(callback);
    }

    public void getBasicSet(long startPosition,
                            long limit,
                            @NonNull Language language,
                            Callback<SetBasicDTO> callback) {
        setService.getBasicSet(startPosition, limit, language)
                .enqueue(callback);
    }*/

}
