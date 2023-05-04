package com.mmasata.eagenerator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerParameter {

    private String name;
    private String in;
    private String type;
    private String format;
    private String example;

    private boolean required;
}
