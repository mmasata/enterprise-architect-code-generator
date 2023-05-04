package com.mmasata.eagenerator;

import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.api.rest.ApiResource;

import java.util.List;

public interface MapperHandler {

    List<ApiResource> mapApiResources();

    List<DTOProperty> mapDtoObjects();
}
