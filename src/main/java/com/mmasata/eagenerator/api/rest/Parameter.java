package com.mmasata.eagenerator.api.rest;

import com.mmasata.eagenerator.api.rest.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {

    private String name;
    private String example;

    private boolean required;

    private DataType dataType;

}
