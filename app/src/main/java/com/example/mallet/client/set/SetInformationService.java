package com.example.mallet.client.set;

import com.example.mallet.utils.Language;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SetInformationService {

    @POST("set")
    Call<Void> getSet(@Query("setId") long setId,
                      @Query("termStartPosition") int termStartPosition,
                      @Query("primaryLanguage") Language primaryLanguage);

}
