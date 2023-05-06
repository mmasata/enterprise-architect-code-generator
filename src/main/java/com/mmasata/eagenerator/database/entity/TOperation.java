package com.mmasata.eagenerator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * JPA entity defining methods/functions of classes and their information such as name, return type, input parameters, etc in EA model.
 */
@Getter
@Entity
@Table(name = "t_operation")
@AttributeOverride(column = @Column(name = "OperationID"), name = "id")
public class TOperation extends AbstractEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private String type;

    @Column(name = "Stereotype")
    private String stereotype;

    @Column(name = "Object_ID")
    private Long objectId;

}
