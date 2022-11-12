package co.edu.sena.repository;

import co.edu.sena.domain.Deduction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DeductionRepositoryWithBagRelationships {
    Optional<Deduction> fetchBagRelationships(Optional<Deduction> deduction);

    List<Deduction> fetchBagRelationships(List<Deduction> deductions);

    Page<Deduction> fetchBagRelationships(Page<Deduction> deductions);
}
