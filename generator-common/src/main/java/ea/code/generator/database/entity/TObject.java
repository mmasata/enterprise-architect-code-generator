package ea.code.generator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_Object")
@AttributeOverride(column = @Column(name = "Object_ID"), name = "id")
public class TObject extends AbstractEntity {

    @Column(name = "Name")
    private String name;

}
