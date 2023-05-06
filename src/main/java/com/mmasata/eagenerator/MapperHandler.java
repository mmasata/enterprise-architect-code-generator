package com.mmasata.eagenerator;

import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.api.rest.ApiResource;

import java.util.List;

/**
 * Interface that every mapper must extend.
 * It defines the methods that the generator framework will call via reflection to get the data for the common-api.
 */
public interface MapperHandler {

    List<ApiResource> mapApiResources();

    List<DTOProperty> mapDtoObjects();
}
