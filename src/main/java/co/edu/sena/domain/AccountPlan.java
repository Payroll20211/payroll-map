package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A AccountPlan.
 */
@Entity
@Table(name = "account_plan")
public class AccountPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @ManyToMany(mappedBy = "accountPlans")
    @JsonIgnoreProperties(value = { "incomes", "accountPlans" }, allowSetters = true)
    private Set<Income> incomes = new HashSet<>();

    @ManyToMany(mappedBy = "accountPlans")
    @JsonIgnoreProperties(value = { "deductions", "accountPlans" }, allowSetters = true)
    private Set<Deduction> deductions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public AccountPlan code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public AccountPlan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Income> getIncomes() {
        return this.incomes;
    }

    public void setIncomes(Set<Income> incomes) {
        if (this.incomes != null) {
            this.incomes.forEach(i -> i.removeAccountPlan(this));
        }
        if (incomes != null) {
            incomes.forEach(i -> i.addAccountPlan(this));
        }
        this.incomes = incomes;
    }

    public AccountPlan incomes(Set<Income> incomes) {
        this.setIncomes(incomes);
        return this;
    }

    public AccountPlan addIncome(Income income) {
        this.incomes.add(income);
        income.getAccountPlans().add(this);
        return this;
    }

    public AccountPlan removeIncome(Income income) {
        this.incomes.remove(income);
        income.getAccountPlans().remove(this);
        return this;
    }

    public Set<Deduction> getDeductions() {
        return this.deductions;
    }

    public void setDeductions(Set<Deduction> deductions) {
        if (this.deductions != null) {
            this.deductions.forEach(i -> i.removeAccountPlan(this));
        }
        if (deductions != null) {
            deductions.forEach(i -> i.addAccountPlan(this));
        }
        this.deductions = deductions;
    }

    public AccountPlan deductions(Set<Deduction> deductions) {
        this.setDeductions(deductions);
        return this;
    }

    public AccountPlan addDeduction(Deduction deduction) {
        this.deductions.add(deduction);
        deduction.getAccountPlans().add(this);
        return this;
    }

    public AccountPlan removeDeduction(Deduction deduction) {
        this.deductions.remove(deduction);
        deduction.getAccountPlans().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountPlan)) {
            return false;
        }
        return id != null && id.equals(((AccountPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountPlan{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
