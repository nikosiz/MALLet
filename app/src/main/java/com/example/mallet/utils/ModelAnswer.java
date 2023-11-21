package com.example.mallet.utils;

import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ModelAnswer {

    @EqualsAndHashCode.Include
    UUID uuid = UUID.randomUUID();
    String answer;
    boolean isCorrect;


}
