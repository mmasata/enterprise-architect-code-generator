package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TObjectRepository extends JpaRepository<TObject, Long> {

    List<TObject> findByPackageIdAndType(Long packageId, String type);

    Optional<TObject> findById(Long id);

    Optional<TObject> findByEaGuid(String eaGuid);

}
