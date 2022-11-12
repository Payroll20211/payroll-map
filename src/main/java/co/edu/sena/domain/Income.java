package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Income.
 */
@Entity
@Table(name = "income")
public class Income implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "income_code", length = 10, nullable = false)
    private String incomeCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @OneToMany(mappedBy = "income")
    @JsonIgnoreProperties(
        value = {
            "user",
            "contract",
            "allergy",
            "socialPayments",
            "positionArl",
            "period",
            "operatorType",
            "operatorMatriz",
            "socialSecurity",
            "employees",
            "income",
            "deduction",
            "documentType",
        },
        allowSetters = true
    )
    private Set<Employee> incomes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_income__account_plan",
        joinColumns = @JoinColumn(name = "income_id"),
        inverseJoinColumns = @JoinColumn(name = "account_plan_id")
    )
    @JsonIgnoreProperties(value = { "incomes", "deductions" }, allowSetters = true)
    private Set<AccountPlan> accountPlans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Income id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIncomeCode() {
        return this.incomeCode;
    }

    public Income incomeCode(String incomeCode) {
        this.setIncomeCode(incomeCode);
        return this;
    }

    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
    }

    public String getDescription() {
        return this.description;
    }

    public Income description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getIncomes() {
        return this.incomes;
    }

    public void setIncomes(Set<Employee> employees) {
        if (this.incomes != null) {
            this.incomes.forEach(i -> i.setIncome(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setIncome(this));
        }
        this.incomes = employees;
    }

    public Income incomes(Set<Employee> employees) {
        this.setIncomes(employees);
        return this;
    }

    public Income addIncome(Employee employee) {
        this.incomes.add(employee);
        employee.setIncome(this);
        return this;
    }

    public Income removeIncome(Employee employee) {
        this.incomes.remove(employee);
        employee.setIncome(null);
        return this;
    }

    public Set<AccountPlan> getAccountPlans() {
        return this.accountPlans;
    }

    public void setAccountPlans(Set<AccountPlan> accountPlans) {
        this.accountPlans = accountPlans;
    }

    public Income accountPlans(Set<AccountPlan> accountPlans) {
        this.setAccountPlans(accountPlans);
        return this;
    }

    public Income addAccountPlan(AccountPlan accountPlan) {
        this.accountPlans.add(accountPlan);
        accountPlan.getIncomes().add(this);
        return this;
    }

    public Income removeAccountPlan(AccountPlan accountPlan) {
        this.accountPlans.remove(accountPlan);
        accountPlan.getIncomes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Income)) {
            return false;
        }
        return id != null && id.equals(((Income) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Income{" +
            "id=" + getId() +
            ", incomeCode='" + getIncomeCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
