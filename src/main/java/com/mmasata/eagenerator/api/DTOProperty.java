package com.mmasata.eagenerator.api;

import com.mmasata.eagenerator.api.enums.DataType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common-api model defining an object or parameter.
 * <p>
 * It stores the data type, name, its parameters.
 */
@Data
public class DTOProperty {

    private String name;
    private String namespace;
    private String example;
    private String defaultValue;
    private String description;

    private DataType dataType;

    private List<String> enumValues = new ArrayList<>();

    private List<Constraint> constraints = new ArrayList<>();

    private Map<String, DTOPropertyWrapper> childProperties = new HashMap<>();

    public DTOProperty addProperty(String key,
                                   DTOPropertyWrapper value) {

        childProperties.put(key, value);
        return this;
    }

    public DTOProperty addEnumValue(String value) {

        enumValues.add(value);
        return this;
    }

}
