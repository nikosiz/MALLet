package com.example.mallet.utils;

import com.agh.api.UserDTO;

public class ModelUserMapper {

    private ModelUserMapper() {}

    public static ModelUser from(UserDTO userDTO){
        return ModelUser.builder()
                .id(userDTO.id())
                .username(userDTO.name())
                .identifier(userDTO.identifier())
                .build();
    }
}
