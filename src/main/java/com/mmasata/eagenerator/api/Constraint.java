package com.mmasata.eagenerator.api;

import com.mmasata.eagenerator.api.enums.ConstraintType;
import lombok.Data;

@Data
public class Constraint {

    private ConstraintType constraintType;

    private String value;

}
