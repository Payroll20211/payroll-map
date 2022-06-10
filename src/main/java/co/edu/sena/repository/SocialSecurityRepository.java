package co.edu.sena.repository;

import co.edu.sena.domain.SocialSecurity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SocialSecurity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialSecurityRepository extends JpaRepository<SocialSecurity, Long> {}
