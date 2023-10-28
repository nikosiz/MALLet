package com.example.mallet.utils;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class ModelUser {
    @NonNull
    Long id;
    @NonNull
    String username;
    String identifier;
}