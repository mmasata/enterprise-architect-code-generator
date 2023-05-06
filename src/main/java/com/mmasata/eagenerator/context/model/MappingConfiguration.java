package com.mmasata.eagenerator.context.model;

import com.mmasata.eagenerator.context.model.enums.MappingType;
import lombok.Data;

/**
 * A context model of the framework generator preserving the parameterization of the mapping strategy.
 */
@Data
public class MappingConfiguration {

    private String profile;

    private MappingType type;

    //future versions - configuration for generic MappingType
}
