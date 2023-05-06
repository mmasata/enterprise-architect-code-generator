package com.mmasata.eagenerator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serializable;

/**
 * JPA entity defining the relationship (including the relationship type) between two classes in the EA model.
 */
@Getter
@Entity
@Table(name = "t_connector")
@AttributeOverride(column = @Column(name = "Connector_ID"), name = "id")
public class TConnector extends AbstractEntity implements Serializable {

    @Column(name = "Name")
    private String name;

    @Column(name = "Connector_Type")
    private String type;

    @Column(name = "Stereotype")
    private String stereotype;

    @Column(name = "SourceCard")
    private String srcCard;

    @Column(name = "DestCard")
    private String destCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Start_Object_ID")
    private TObject startObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "End_Object_ID")
    private TObject endObject;

}
