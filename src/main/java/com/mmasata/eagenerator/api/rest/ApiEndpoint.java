package com.mmasata.eagenerator.api.rest;

import com.mmasata.eagenerator.api.rest.enums.HttpMethod;
import com.mmasata.eagenerator.api.rest.enums.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiEndpoint {

    private String name;
    private String path;
    private String description;

    private HttpMethod httpMethod;
    private HttpMessage request;

    private Map<HttpStatus, HttpMessage> responses = new HashMap<>();

    private List<Parameter> httpHeaders = new ArrayList<>();
    private List<Parameter> pathParams = new ArrayList<>();
    private List<Parameter> queryParams = new ArrayList<>();

}
