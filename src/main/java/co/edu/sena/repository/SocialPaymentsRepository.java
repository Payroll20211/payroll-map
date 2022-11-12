package co.edu.sena.repository;

import co.edu.sena.domain.SocialPayments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SocialPayments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialPaymentsRepository extends JpaRepository<SocialPayments, Long> {}
