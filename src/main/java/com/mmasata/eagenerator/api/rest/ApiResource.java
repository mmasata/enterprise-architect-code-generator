package com.mmasata.eagenerator.api.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Common-api model defining an API resource.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResource {

    private String path;
    private String name;
    private String description;

    private List<ApiEndpoint> endpoints;
}
