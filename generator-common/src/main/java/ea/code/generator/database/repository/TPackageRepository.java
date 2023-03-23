package ea.code.generator.database.repository;

import ea.code.generator.database.entity.TPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TPackageRepository extends JpaRepository<TPackage, Long> {

    TPackage findTPackageByParentIdAndName(Long parentId, String name);

    TPackage findTPackageById(Long id);

}
