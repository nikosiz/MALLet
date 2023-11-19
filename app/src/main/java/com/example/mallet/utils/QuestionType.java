package com.example.mallet.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum QuestionType {
    TERM,
    DEFINITION,
    TRANSLATION;

    private static final List<QuestionType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));

    private static final List<QuestionType> VALUES_WITHOUT_DEFINITION =
            Collections.unmodifiableList(Arrays.asList(TERM, TRANSLATION));
    private static final Random RANDOM = new Random();

    public static QuestionType randomType() {
        return VALUES.get(RANDOM.nextInt(VALUES.size()));
    }

    public static QuestionType randomTypeWithoutDefinitionType() {
        return VALUES_WITHOUT_DEFINITION.get(RANDOM.nextInt(VALUES_WITHOUT_DEFINITION.size()));
    }

}