package ea.code.generator.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Column(name = "ID")
    private long id;
}
