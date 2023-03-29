package ea.code.generator.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
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

    @ManyToOne
    @JoinColumn(name = "Object_ID")
    private TObject object;

}
