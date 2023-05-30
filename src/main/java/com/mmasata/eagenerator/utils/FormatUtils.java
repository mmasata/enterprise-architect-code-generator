package com.mmasata.eagenerator.utils;

import lombok.NoArgsConstructor;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class FormatUtils {

    private static final String SPACE = " ";
    private static final String DOT = ".";
    private static final String DASH = "-";

    public static String toCamelCase(String value) {

        var words = value.split("");
        var splitCharacters = Set.of(SPACE, DASH, DOT);
        var sb = new StringBuilder();
        var isNewWord = false;

        for (var i = 0; i < value.length(); i++) {
            var word = words[i];

            if (i == 0) {
                sb.append(word.toLowerCase());
                continue;
            }

            if (splitCharacters.contains(word)) {
                isNewWord = true;
                continue;
            }

            if (isNewWord) {
                sb.append(word.toUpperCase());
                isNewWord = false;
                continue;
            }

            sb.append(word);
        }

        return sb.toString();
    }

}
