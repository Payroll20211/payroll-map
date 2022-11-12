package co.edu.sena.repository;

import co.edu.sena.domain.Income;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IncomeRepositoryWithBagRelationships {
    Optional<Income> fetchBagRelationships(Optional<Income> income);

    List<Income> fetchBagRelationships(List<Income> incomes);

    Page<Income> fetchBagRelationships(Page<Income> incomes);
}
