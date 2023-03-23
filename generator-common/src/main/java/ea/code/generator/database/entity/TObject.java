package ea.code.generator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStereotype() {
        return stereotype;
    }

    public String getEaGuid() {
        return eaGuid;
    }
}
