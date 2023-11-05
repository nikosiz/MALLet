package com.example.mallet.utils;

import java.util.Set;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ModelMultipleChoice {
    String question;
    Set<ModelAnswer> answers;
}

