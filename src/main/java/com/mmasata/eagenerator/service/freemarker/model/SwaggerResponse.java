package com.mmasata.eagenerator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerResponse {

    private String code;
    private String contentType;
    private String schemaName;
    private String description;

    private boolean isArray;
}
