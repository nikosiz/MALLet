package com.example.mallet.utils;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ModelAnswer {
    String answer;
    boolean isCorrect;
}
