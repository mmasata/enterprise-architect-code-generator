package com.mmasata.eagenerator.service.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AvroSchemaDTO {

    private String fileName;

    private Map<String, Object> freemarkerVariables = new HashMap<>();

    public AvroSchemaDTO addVariable(String key,
                                     Object value) {

        freemarkerVariables.put(key, value);
        return this;
    }

}
