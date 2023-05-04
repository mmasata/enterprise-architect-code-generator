package com.mmasata.eagenerator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerPath {

    private String path;

    private SwaggerEndpoint get;
    private SwaggerEndpoint post;
    private SwaggerEndpoint put;
    private SwaggerEndpoint delete;
    private SwaggerEndpoint patch;
    private SwaggerEndpoint options;

}
