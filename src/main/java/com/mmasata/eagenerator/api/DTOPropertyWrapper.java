package com.mmasata.eagenerator.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common-api model defining the relationship of an object in a given context.
 * <p>
 * It enriches the information about whether an object is mandatory and its multiplicity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOPropertyWrapper {

    private DTOProperty property;

    private boolean required;
    private boolean isArray;

}
