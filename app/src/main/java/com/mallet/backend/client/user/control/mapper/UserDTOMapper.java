package com.mallet.backend.client.user.control.mapper;

import com.agh.api.UserDTO;

public class UserDTOMapper {

    private UserDTOMapper() {}

    public static UserDTO from(Long id){
        return UserDTO.builder()
                .id(id)
                .build();
    }
}
