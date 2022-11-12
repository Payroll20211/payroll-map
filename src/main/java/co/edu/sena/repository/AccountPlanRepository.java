package co.edu.sena.repository;

import co.edu.sena.domain.AccountPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountPlanRepository extends JpaRepository<AccountPlan, Long> {}
