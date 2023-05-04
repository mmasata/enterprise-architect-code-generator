package com.mmasata.eagenerator.utils;

import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConfigUtils {

    @Nullable
    public static Object getConfigParameterOrNull(Map<String, Object> params,
                                                  String key) {

        if (params.containsKey(key)) {
            return params.get(key);
        }

        return null;
    }

}
