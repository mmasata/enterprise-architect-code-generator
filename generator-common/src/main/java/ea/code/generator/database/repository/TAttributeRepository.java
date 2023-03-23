package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TAttributeRepository extends JpaRepository<TAttribute, Long> {

    List<TAttribute> findByObjectId(Long objectId);
}
