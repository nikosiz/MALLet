package com.example.mallet.client.user;

import com.agh.api.UserLogInDTO;

public class UserLogInDTOMapper {

    private UserLogInDTOMapper() {}

    public static UserLogInDTO from(String email,
                                    String password) {
        return UserLogInDTO.builder()
                .email(email)
                .password(password)
                .build();
    }

}
