package com.mmasata.eagenerator.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Abstract JPA entity.
 */
@Getter
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Column(name = "ID")
    private long id;
}
