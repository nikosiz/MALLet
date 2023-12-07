package com.mallet.mallet.backend.client.user.boundary;

import com.agh.api.GroupBasicDTO;
import com.agh.api.SetBasicDTO;
import com.agh.api.SetCreateDTO;
import com.agh.api.UserDTO;
import com.agh.api.UserDetailDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;
import com.mallet.mallet.backend.client.configuration.RetrofitClient;
import com.mallet.mallet.backend.client.group.control.mapper.SetCreateDTOMapper;
import com.mallet.mallet.backend.client.user.control.UserService;
import com.mallet.mallet.backend.client.user.control.mapper.UserLogInDTOMapper;
import com.mallet.mallet.backend.client.user.control.mapper.UserRegistrationDTOMapper;
import com.mallet.mallet.backend.entity.set.SetCreateContainer;

import java.util.List;
import java.util.Set;

import lombok.NonNull;
import retrofit2.Callback;

public class UserServiceImpl {

    private final UserService userService;

    public UserServiceImpl(String credential) {
        this.userService = RetrofitClient.getRetrofitClient(credential)
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

    public void getUserGroups(long startPosition,
                            long limit,
                            Callback<GroupBasicDTO> callback) {
        userService.getUserGroups(startPosition, limit)
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

    public void createUserSet(@NonNull SetCreateContainer setCreateContainer,
                              Callback<Long> callback) {
        SetCreateDTO setCreateDTO = SetCreateDTOMapper.from(setCreateContainer);

        userService.createSet(setCreateDTO)
                .enqueue(callback);
    }

    public void addUserKnownTerm(@NonNull Set<Long> userKnownTermIds,
                                 Callback<Void> callback) {
        userService.addUserKnownTerm(userKnownTermIds)
                .enqueue(callback);
    }

    public void get(@NonNull String username,
                    Callback<List<UserDTO>> callback) {
        userService.get(username)
                .enqueue(callback);
    }

    public void deleteUser(long id,
                           Callback<Void> callback) {
        userService.delete(id)
                .enqueue(callback);
    }

}
