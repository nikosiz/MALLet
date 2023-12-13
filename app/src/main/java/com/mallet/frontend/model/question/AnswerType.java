package com.mallet.frontend.model.question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum AnswerType {
    TERM,
    DEFINITION,
    TRANSLATION;

    private static final List<AnswerType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));

    private static final List<AnswerType> VALUES_WITHOUT_DEFINITION =
            Collections.unmodifiableList(Arrays.asList(TERM, TRANSLATION));
    private static final Random RANDOM = new Random();

    public static AnswerType randomType() {
        return VALUES.get(RANDOM.nextInt(VALUES.size()));
    }

    public static AnswerType randomTypeWithoutDefinitionType() {
        return VALUES_WITHOUT_DEFINITION.get(RANDOM.nextInt(VALUES_WITHOUT_DEFINITION.size()));
    }

}