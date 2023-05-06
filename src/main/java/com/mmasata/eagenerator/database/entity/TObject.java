package com.mmasata.eagenerator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.List;

/**
 * JPA entity defining the basic building blocks in EA model. These include class, folder, data type and others.
 */
@Getter
@Entity
@Table(name = "t_object")
@AttributeOverride(column = @Column(name = "Object_ID"), name = "id")
public class TObject extends AbstractEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "Object_Type")
    private String type;

    @Column(name = "Stereotype")
    private String stereotype;

    @Column(name = "ea_guid")
    private String eaGuid;

    @Column(name = "Package_ID")
    private Long packageId;

    @Column(name = "ParentID")
    private Long parentObjectId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Object_ID")
    private List<TAttribute> attributes;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Start_Object_ID")
    private List<TConnector> sourceConns;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "End_Object_ID")
    private List<TConnector> endConns;

}
