package com.example.mallet.backend.client.user.control;

import com.agh.api.GroupBasicDTO;
import com.agh.api.SetBasicDTO;
import com.agh.api.SetCreateDTO;
import com.agh.api.UserDTO;
import com.agh.api.UserDetailDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {

    @POST(UserServiceConstants.REGISTRATION_PATH)
    Call<Void> register(@Body UserRegistrationDTO registrationDTO);

    @POST(UserServiceConstants.LOGIN_PATH)
    Call<UserDetailDTO> login(@Body UserLogInDTO logInDTO);

    @GET(UserServiceConstants.USER_SET_PATH)
    Call<SetBasicDTO> getUserSets(@Query(UserServiceConstants.START_POSITION_PARAM) long startPosition,
                                  @Query(UserServiceConstants.LIMIT_PARAM) long limit);

    @PUT(UserServiceConstants.USER_SET_PATH)
    Call<Void> addSetToUserSets(@Query(UserServiceConstants.SET_ID_PARAM) long setId);

    @DELETE(UserServiceConstants.USER_SET_PATH)
    Call<Void> deleteUserSet(@Query(UserServiceConstants.SET_ID_PARAM) long setId);

    @POST(UserServiceConstants.USER_SET_PATH)
    Call<Void> createSet(@Body SetCreateDTO setCreateDTO);

    @PUT(UserServiceConstants.USER_TERM_ADD_PATH)
    Call<Void> addUserKnownTerm(@Body Set<Long> userKnownTermIds);

    @GET(UserServiceConstants.USER_PATH)
    Call<List<UserDTO>> get(@Query(UserServiceConstants.USERNAME_PARAM) String username);

    @GET(UserServiceConstants.USER_GROUP_PATH)
    Call<GroupBasicDTO> getUserGroups(@Query(UserServiceConstants.START_POSITION_PARAM) long startPosition,
                                      @Query(UserServiceConstants.LIMIT_PARAM) long limit);
}
