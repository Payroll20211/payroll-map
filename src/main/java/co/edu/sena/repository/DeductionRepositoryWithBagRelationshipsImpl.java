package co.edu.sena.repository;

import co.edu.sena.domain.Deduction;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DeductionRepositoryWithBagRelationshipsImpl implements DeductionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Deduction> fetchBagRelationships(Optional<Deduction> deduction) {
        return deduction.map(this::fetchAccountPlans);
    }

    @Override
    public Page<Deduction> fetchBagRelationships(Page<Deduction> deductions) {
        return new PageImpl<>(fetchBagRelationships(deductions.getContent()), deductions.getPageable(), deductions.getTotalElements());
    }

    @Override
    public List<Deduction> fetchBagRelationships(List<Deduction> deductions) {
        return Optional.of(deductions).map(this::fetchAccountPlans).orElse(Collections.emptyList());
    }

    Deduction fetchAccountPlans(Deduction result) {
        return entityManager
            .createQuery(
                "select deduction from Deduction deduction left join fetch deduction.accountPlans where deduction is :deduction",
                Deduction.class
            )
            .setParameter("deduction", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Deduction> fetchAccountPlans(List<Deduction> deductions) {
        return entityManager
            .createQuery(
                "select distinct deduction from Deduction deduction left join fetch deduction.accountPlans where deduction in :deductions",
                Deduction.class
            )
            .setParameter("deductions", deductions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
