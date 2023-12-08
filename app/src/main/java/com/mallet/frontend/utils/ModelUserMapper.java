package com.mallet.frontend.utils;

import com.agh.api.UserDTO;
import com.mallet.frontend.model.user.ModelUser;

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
