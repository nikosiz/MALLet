package com.example.mallet.client.user;

import com.agh.api.UserInformationDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("user/registration")
    Call<Void> register(@Body UserRegistrationDTO registrationDTO);

    @POST("user/login")
    Call<UserInformationDTO> login(@Body UserLogInDTO logInDTO);

}
