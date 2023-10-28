package com.example.mallet.utils;

import com.agh.api.UserDTO;

public class ModelUser {
    private Long id;
    private String username;
    private String identifier;

    public ModelUser(UserDTO userDTO) {
        this.id = userDTO.id();
        this.username = userDTO.name();
        this.identifier = identifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}