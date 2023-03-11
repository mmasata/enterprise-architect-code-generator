package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TObjectRepository extends JpaRepository<TObject, Long> {
}
