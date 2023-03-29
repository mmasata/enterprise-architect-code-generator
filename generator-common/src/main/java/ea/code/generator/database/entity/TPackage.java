package ea.code.generator.database.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "t_package")
@AttributeOverride(column = @Column(name = "Package_ID"), name = "id")
public class TPackage extends AbstractEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "ea_guid")
    private String eaGuid;

    @Column(name = "Parent_ID")
    private Long parentId;

}
