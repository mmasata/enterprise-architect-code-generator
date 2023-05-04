package com.mmasata.eagenerator.context.model;

import com.mmasata.eagenerator.context.model.enums.MappingType;
import lombok.Data;

@Data
public class MappingConfiguration {

    private String profile;

    private MappingType type;

    //TODO configuration for generic MappingType
}
