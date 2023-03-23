package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TConnector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TConnectorRepository extends JpaRepository<TConnector, Long> {

    List<TConnector> findByStartObjectId(Long startObjectId);

    List<TConnector> findByEndObjectId(Long endObjectId);
}
