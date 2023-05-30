package com.mmasata.eagenerator.context.model;

import com.mmasata.eagenerator.context.model.enums.ControllerType;
import com.mmasata.eagenerator.context.model.enums.DtoType;
import lombok.Data;

@Data
public class JavaSpringConfiguration {

    private ControllerType controllerType;

    private DtoType dtoType;

    private String packageName;

    private Pom pom;
}
