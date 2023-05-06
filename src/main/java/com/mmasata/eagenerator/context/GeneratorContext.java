package com.mmasata.eagenerator.context;

import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.context.model.GeneratorConfiguration;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Defines the data important for the generator, defining its behavior - the context of the generator framework.
 * <p>
 * It stores the configuration from the input, and common-api.
 */
@Data
public class GeneratorContext {

    private GeneratorConfiguration configuration;

    private List<ApiResource> apiResources = Collections.emptyList();

    private List<DTOProperty> dtoObjects = Collections.emptyList();

}
