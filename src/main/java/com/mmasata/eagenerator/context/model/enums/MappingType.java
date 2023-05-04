package com.mmasata.eagenerator.context.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

public enum MappingType {

    GENERIC,
    CUSTOM,
    ;

    @JsonCreator
    public static MappingType of(String type) {

        if (StringUtils.isEmpty(type) || GENERIC.name().equalsIgnoreCase(type)) {
            return GENERIC;
        }

        return CUSTOM;
    }

}
