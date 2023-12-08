package com.mallet.backend.utils;

import com.agh.api.Language;
import com.mallet.backend.exception.MalletException;

import java.util.Arrays;

public class LanguageConverter {

    private LanguageConverter() {}

    public static Language convert(com.mallet.backend.entity.term.Language language) {
        return Arrays.stream(Language.values())
                .filter(languagee -> languagee.name().equals(language.name()))
                .findAny()
                .orElseThrow(() -> new MalletException("Matching language was not found"));
    }

}
