package com.example.mallet.utils;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
@EqualsAndHashCode
public class ModelUser {
    @NonNull
    Long id;
    @NonNull
    String username;
    String identifier;

    @Override
    public String toString() {
        return identifier;
    }
}