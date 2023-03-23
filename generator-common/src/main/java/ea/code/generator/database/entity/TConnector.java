package ea.code.generator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

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

    @Column(name = "Start_Object_ID")
    private Long startObjectId;

    @Column(name = "End_Object_ID")
    private Long endObjectId;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStereotype() {
        return stereotype;
    }

    public String getSrcCard() {
        return srcCard;
    }

    public String getDestCard() {
        return destCard;
    }

    public Long getStartObjectId() {
        return startObjectId;
    }

    public Long getEndObjectId() {
        return endObjectId;
    }
}
