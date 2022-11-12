package co.edu.sena.repository;

import co.edu.sena.domain.OperatorType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OperatorType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperatorTypeRepository extends JpaRepository<OperatorType, Long> {}
