package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TOperationRepository extends JpaRepository<TOperation, Long> {

    List<TOperation> findByObjectId(Long objectId);
}
