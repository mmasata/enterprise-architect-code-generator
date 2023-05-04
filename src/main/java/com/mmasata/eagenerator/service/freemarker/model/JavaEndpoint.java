package com.mmasata.eagenerator.service.freemarker.model;

import lombok.Data;

import java.util.List;

@Data
public class JavaEndpoint {

    private String httpMethod;
    private String path;
    private String returnType;
    private String methodName;

    private List<String> params;

}
