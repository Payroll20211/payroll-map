package co.edu.sena.repository;

import co.edu.sena.domain.Allergy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Allergy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {}
