package com.mmasata.eagenerator.service.freemarker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaParam {

    private boolean isNullable;

    private String type;
    private String name;
}
