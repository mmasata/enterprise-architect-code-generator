package com.mmasata.eagenerator.api.rest;

import com.mmasata.eagenerator.api.Constraint;
import com.mmasata.eagenerator.api.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Common-api model defining an API parameter ( query, header, path variable).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {

    private String name;
    private String description;
    private String defaultValue;
    private String example;

    private boolean required;

    private DataType dataType;

    private List<Constraint> constraints;

}
