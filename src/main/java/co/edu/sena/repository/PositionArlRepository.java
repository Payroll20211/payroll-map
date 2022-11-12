package co.edu.sena.repository;

import co.edu.sena.domain.PositionArl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PositionArl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PositionArlRepository extends JpaRepository<PositionArl, Long> {}
