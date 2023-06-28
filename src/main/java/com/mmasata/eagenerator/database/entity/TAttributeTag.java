package com.mmasata.eagenerator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "t_attributetag")
@AttributeOverride(column = @Column(name = "PropertyID"), name = "id")
public class TAttributeTag extends AbstractEntity {

    @Column(name = "Property")
    private String property;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "ElementID")
    private TAttribute tAttribute;
}
