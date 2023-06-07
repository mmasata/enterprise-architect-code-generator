package com.mmasata.eagenerator.context.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaType {

    JSON("application/json"),
    XML("application/xml"),
    ;

    private final String value;
}
