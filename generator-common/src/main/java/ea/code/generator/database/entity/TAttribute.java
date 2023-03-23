package ea.code.generator.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_attribute")
public class TAttribute extends AbstractEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private String type;

    @Column(name = "Stereotype")
    private String stereotype;

    @Column(name = "`Default`")
    private String initValue;

    @Column(name = "Object_ID")
    private Long objectId;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStereotype() {
        return stereotype;
    }

    public String getInitValue() {
        return initValue;
    }

    public Long getObjectId() {
        return objectId;
    }
}
