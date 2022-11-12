package co.edu.sena.repository;

import co.edu.sena.domain.OperatorMatriz;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OperatorMatriz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperatorMatrizRepository extends JpaRepository<OperatorMatriz, Long> {}
