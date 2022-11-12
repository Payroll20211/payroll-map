package co.edu.sena.repository;

import co.edu.sena.domain.Income;
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
public class IncomeRepositoryWithBagRelationshipsImpl implements IncomeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Income> fetchBagRelationships(Optional<Income> income) {
        return income.map(this::fetchAccountPlans);
    }

    @Override
    public Page<Income> fetchBagRelationships(Page<Income> incomes) {
        return new PageImpl<>(fetchBagRelationships(incomes.getContent()), incomes.getPageable(), incomes.getTotalElements());
    }

    @Override
    public List<Income> fetchBagRelationships(List<Income> incomes) {
        return Optional.of(incomes).map(this::fetchAccountPlans).orElse(Collections.emptyList());
    }

    Income fetchAccountPlans(Income result) {
        return entityManager
            .createQuery("select income from Income income left join fetch income.accountPlans where income is :income", Income.class)
            .setParameter("income", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Income> fetchAccountPlans(List<Income> incomes) {
        return entityManager
            .createQuery(
                "select distinct income from Income income left join fetch income.accountPlans where income in :incomes",
                Income.class
            )
            .setParameter("incomes", incomes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
