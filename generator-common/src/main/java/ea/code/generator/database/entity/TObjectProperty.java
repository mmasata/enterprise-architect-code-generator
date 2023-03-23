package ea.code.generator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_objectproperties")
@AttributeOverride(column = @Column(name = "PropertyID"), name = "id")
public class TObjectProperty extends AbstractEntity {

    @Column(name = "Property")
    private String name;

    @Column(name = "Value")
    private String value;

    @Column(name = "Object_ID")
    private Long objectId;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Long getObjectId() {
        return objectId;
    }
}
