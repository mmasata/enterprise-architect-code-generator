package com.mmasata.eagenerator.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOPropertyWrapper {

    private DTOProperty property;

    private boolean required;
    private boolean isArray;

}
