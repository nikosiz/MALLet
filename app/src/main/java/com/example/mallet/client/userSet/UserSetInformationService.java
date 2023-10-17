package com.example.mallet.client.userSet;

import com.agh.api.UserSetInformationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserSetInformationService {

    //todo naprawić jak stary puczynski zrobi
    @POST("user/set/add")
    Call<Void> addSet(@Body UserSetInformationDTO userSetDTO);

    @POST("user/set/get")
    Call<Void> getSet(@Body UserSetInformationDTO logInDTO);

}
