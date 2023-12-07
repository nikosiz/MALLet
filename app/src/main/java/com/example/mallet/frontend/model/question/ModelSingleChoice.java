package com.example.mallet.frontend.model.question;

import java.util.Set;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ModelSingleChoice {
    String question;
    Set<ModelAnswer> answers;
}

