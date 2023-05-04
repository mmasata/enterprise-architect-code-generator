package com.mmasata.eagenerator.context;

import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.context.model.GeneratorConfiguration;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class GeneratorContext {

    private GeneratorConfiguration configuration;

    private List<ApiResource> apiResources = Collections.emptyList();

    private List<DTOProperty> dtoObjects = Collections.emptyList();

}
