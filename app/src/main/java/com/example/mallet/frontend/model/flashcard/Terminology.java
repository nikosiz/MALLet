package com.example.mallet.frontend.model.flashcard;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Terminology {

     String termName;
     String termDefinition;
     String termTranslation;

}