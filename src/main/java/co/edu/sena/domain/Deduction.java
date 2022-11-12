package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Deduction.
 */
@Entity
@Table(name = "deduction")
public class Deduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "deduction_code", length = 10, nullable = false)
    private String deductionCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @OneToMany(mappedBy = "deduction")
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
    private Set<Employee> deductions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_deduction__account_plan",
        joinColumns = @JoinColumn(name = "deduction_id"),
        inverseJoinColumns = @JoinColumn(name = "account_plan_id")
    )
    @JsonIgnoreProperties(value = { "incomes", "deductions" }, allowSetters = true)
    private Set<AccountPlan> accountPlans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deduction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeductionCode() {
        return this.deductionCode;
    }

    public Deduction deductionCode(String deductionCode) {
        this.setDeductionCode(deductionCode);
        return this;
    }

    public void setDeductionCode(String deductionCode) {
        this.deductionCode = deductionCode;
    }

    public String getDescription() {
        return this.description;
    }

    public Deduction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getDeductions() {
        return this.deductions;
    }

    public void setDeductions(Set<Employee> employees) {
        if (this.deductions != null) {
            this.deductions.forEach(i -> i.setDeduction(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setDeduction(this));
        }
        this.deductions = employees;
    }

    public Deduction deductions(Set<Employee> employees) {
        this.setDeductions(employees);
        return this;
    }

    public Deduction addDeduction(Employee employee) {
        this.deductions.add(employee);
        employee.setDeduction(this);
        return this;
    }

    public Deduction removeDeduction(Employee employee) {
        this.deductions.remove(employee);
        employee.setDeduction(null);
        return this;
    }

    public Set<AccountPlan> getAccountPlans() {
        return this.accountPlans;
    }

    public void setAccountPlans(Set<AccountPlan> accountPlans) {
        this.accountPlans = accountPlans;
    }

    public Deduction accountPlans(Set<AccountPlan> accountPlans) {
        this.setAccountPlans(accountPlans);
        return this;
    }

    public Deduction addAccountPlan(AccountPlan accountPlan) {
        this.accountPlans.add(accountPlan);
        accountPlan.getDeductions().add(this);
        return this;
    }

    public Deduction removeAccountPlan(AccountPlan accountPlan) {
        this.accountPlans.remove(accountPlan);
        accountPlan.getDeductions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deduction)) {
            return false;
        }
        return id != null && id.equals(((Deduction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deduction{" +
            "id=" + getId() +
            ", deductionCode='" + getDeductionCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
