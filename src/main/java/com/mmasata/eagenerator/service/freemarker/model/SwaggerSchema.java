package com.mmasata.eagenerator.service.freemarker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SwaggerSchema {

    private String name;
    private String type;
    private String format;
    private String example;

    private boolean isArray;

    private List<String> enumValues = new ArrayList<>();

    private List<String> required = new ArrayList<>();

    private List<SwaggerSchema> childs = new ArrayList<>();
}
