package com.mmasata.eagenerator.service.freemarker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AvroSchema {

    private String namespace;
    private String name;
    private String type;
    private String logicalType;

    private boolean isArray;

    private List<String> enumValues = new ArrayList<>();

    private Map<String, AvroSchema> fields = new HashMap<>();


    public AvroSchema addField(String key,
                               AvroSchema value) {

        fields.put(key, value);
        return this;
    }

}
