package com.example.mallet.backend.client.user.boundary;

import com.agh.api.SetBasicDTO;
import com.agh.api.UserDetailDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;
import com.example.mallet.backend.client.configuration.RetrofitClient;
import com.example.mallet.backend.client.user.control.UserLogInDTOMapper;
import com.example.mallet.backend.client.user.control.UserRegistrationDTOMapper;
import com.example.mallet.backend.client.user.control.UserService;

import java.util.Set;

import lombok.NonNull;
import retrofit2.Callback;

public class UserServiceImpl {

    private final UserService userService;

    public UserServiceImpl() {
        this.userService = RetrofitClient.getRetrofitClient()
                .create(UserService.class);
    }

    public void signUp(@NonNull String username,
                       @NonNull String password,
                       @NonNull String email,
                       Callback<Void> callback) {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOMapper.from(username, password, email);

        userService.register(userRegistrationDTO)
                .enqueue(callback);

    }

    public void login(@NonNull String email,
                      @NonNull String password,
                      Callback<UserDetailDTO> callback) {
        UserLogInDTO userLogInDTO = UserLogInDTOMapper.from(email, password);

        userService.login(userLogInDTO)
                .enqueue(callback);
    }

    public void getUserSets(long startPosition,
                            long limit,
                            Callback<SetBasicDTO> callback) {
        userService.getUserSets(startPosition, limit)
                .enqueue(callback);
    }

    public void addSetToUserSets(@NonNull Long setId,
                                 Callback<Void> callback) {
        userService.addSetToUserSets(setId)
                .enqueue(callback);
    }

    public void deleteUserSet(@NonNull Long setId,
                              Callback<Void> callback) {
        userService.deleteUserSet(setId)
                .enqueue(callback);
    }

    public void addUserKnownTerm(@NonNull Set<Long> userKnownTermIds,
                                 Callback<Void> callback) {
        userService.addUserKnownTerm(userKnownTermIds)
                .enqueue(callback);
    }

}
