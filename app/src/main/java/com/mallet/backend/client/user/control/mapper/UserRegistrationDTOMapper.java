package com.mallet.backend.client.user.control.mapper;

import com.agh.api.UserRegistrationDTO;

public class UserRegistrationDTOMapper {

    private UserRegistrationDTOMapper() {}

    public static UserRegistrationDTO from(String username,
                                           String password,
                                           String email) {
        return UserRegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

}
