package com.mmasata.eagenerator.annotations;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EntityScan("com.mmasata.eagenerator.database.entity")
public @interface EnableGeneratorJpaEntities {
}
