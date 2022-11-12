package co.edu.sena.repository;

import co.edu.sena.domain.ProjectMaster;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProjectMaster entity.
 */
@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, Long> {
    default Optional<ProjectMaster> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProjectMaster> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProjectMaster> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct projectMaster from ProjectMaster projectMaster left join fetch projectMaster.costCenter",
        countQuery = "select count(distinct projectMaster) from ProjectMaster projectMaster"
    )
    Page<ProjectMaster> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct projectMaster from ProjectMaster projectMaster left join fetch projectMaster.costCenter")
    List<ProjectMaster> findAllWithToOneRelationships();

    @Query("select projectMaster from ProjectMaster projectMaster left join fetch projectMaster.costCenter where projectMaster.id =:id")
    Optional<ProjectMaster> findOneWithToOneRelationships(@Param("id") Long id);
}
