package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TObjectProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TObjectPropertyRepository extends JpaRepository<TObjectProperty, Long> {

    List<TObjectProperty> findByObjectId(Long objectId);
}
