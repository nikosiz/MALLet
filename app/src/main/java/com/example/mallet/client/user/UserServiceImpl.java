package com.example.mallet.client.user;

import com.agh.api.UserInformationDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;
import com.example.mallet.client.configuration.RetrofitClient;


import retrofit2.Callback;

public class UserServiceImpl {

    private final UserService userService;

    public UserServiceImpl() {
        this.userService = RetrofitClient.getRetrofitClient()
                .create(UserService.class);
    }

    public void signUp(String username,
                       String password,
                       String email,
                       Callback<Void> callback) {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOMapper.from(username, password, email);

        userService.register(userRegistrationDTO).enqueue(callback);

    }

    public void login(String email,
                      String password,
                      Callback<UserInformationDTO> callback) {
        UserLogInDTO userLogInDTO = UserLogInDTOMapper.from(email, password);

        userService.login(userLogInDTO).enqueue(callback);
    }

}
