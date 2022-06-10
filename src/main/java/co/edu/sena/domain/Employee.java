package co.edu.sena.domain;

import co.edu.sena.domain.enumeration.StateEmployee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "complete_name", length = 100, nullable = false)
    private String completeName;

    @NotNull
    @Size(max = 100)
    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private ZonedDateTime dateStart;

    @NotNull
    @Size(max = 50)
    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @NotNull
    @Column(name = "mobile", nullable = false)
    private Integer mobile;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state_employee", nullable = false)
    private StateEmployee stateEmployee;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Contract contract;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Allergy allergy;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private SocialPayments socialPayments;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private PositionArl positionArl;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Period period;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private OperatorType operatorType;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private OperatorMatriz operatorMatriz;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private SocialSecurity socialSecurity;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "costCenter", "employee" }, allowSetters = true)
    private Set<ProjectMaster> employees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "incomes", "accountPlans" }, allowSetters = true)
    private Income income;

    @ManyToOne
    @JsonIgnoreProperties(value = { "deductions", "accountPlans" }, allowSetters = true)
    private Deduction deduction;

    @ManyToOne
    @JsonIgnoreProperties(value = { "documentTypes" }, allowSetters = true)
    private DocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompleteName() {
        return this.completeName;
    }

    public Employee completeName(String completeName) {
        this.setCompleteName(completeName);
        return this;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getAddress() {
        return this.address;
    }

    public Employee address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ZonedDateTime getDateStart() {
        return this.dateStart;
    }

    public Employee dateStart(ZonedDateTime dateStart) {
        this.setDateStart(dateStart);
        return this;
    }

    public void setDateStart(ZonedDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public String getCity() {
        return this.city;
    }

    public Employee city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getMobile() {
        return this.mobile;
    }

    public Employee mobile(Integer mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public StateEmployee getStateEmployee() {
        return this.stateEmployee;
    }

    public Employee stateEmployee(StateEmployee stateEmployee) {
        this.setStateEmployee(stateEmployee);
        return this;
    }

    public void setStateEmployee(StateEmployee stateEmployee) {
        this.stateEmployee = stateEmployee;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Employee contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Allergy getAllergy() {
        return this.allergy;
    }

    public void setAllergy(Allergy allergy) {
        this.allergy = allergy;
    }

    public Employee allergy(Allergy allergy) {
        this.setAllergy(allergy);
        return this;
    }

    public SocialPayments getSocialPayments() {
        return this.socialPayments;
    }

    public void setSocialPayments(SocialPayments socialPayments) {
        this.socialPayments = socialPayments;
    }

    public Employee socialPayments(SocialPayments socialPayments) {
        this.setSocialPayments(socialPayments);
        return this;
    }

    public PositionArl getPositionArl() {
        return this.positionArl;
    }

    public void setPositionArl(PositionArl positionArl) {
        this.positionArl = positionArl;
    }

    public Employee positionArl(PositionArl positionArl) {
        this.setPositionArl(positionArl);
        return this;
    }

    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Employee period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public OperatorType getOperatorType() {
        return this.operatorType;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public Employee operatorType(OperatorType operatorType) {
        this.setOperatorType(operatorType);
        return this;
    }

    public OperatorMatriz getOperatorMatriz() {
        return this.operatorMatriz;
    }

    public void setOperatorMatriz(OperatorMatriz operatorMatriz) {
        this.operatorMatriz = operatorMatriz;
    }

    public Employee operatorMatriz(OperatorMatriz operatorMatriz) {
        this.setOperatorMatriz(operatorMatriz);
        return this;
    }

    public SocialSecurity getSocialSecurity() {
        return this.socialSecurity;
    }

    public void setSocialSecurity(SocialSecurity socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public Employee socialSecurity(SocialSecurity socialSecurity) {
        this.setSocialSecurity(socialSecurity);
        return this;
    }

    public Set<ProjectMaster> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<ProjectMaster> projectMasters) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setEmployee(null));
        }
        if (projectMasters != null) {
            projectMasters.forEach(i -> i.setEmployee(this));
        }
        this.employees = projectMasters;
    }

    public Employee employees(Set<ProjectMaster> projectMasters) {
        this.setEmployees(projectMasters);
        return this;
    }

    public Employee addEmployee(ProjectMaster projectMaster) {
        this.employees.add(projectMaster);
        projectMaster.setEmployee(this);
        return this;
    }

    public Employee removeEmployee(ProjectMaster projectMaster) {
        this.employees.remove(projectMaster);
        projectMaster.setEmployee(null);
        return this;
    }

    public Income getIncome() {
        return this.income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public Employee income(Income income) {
        this.setIncome(income);
        return this;
    }

    public Deduction getDeduction() {
        return this.deduction;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    public Employee deduction(Deduction deduction) {
        this.setDeduction(deduction);
        return this;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Employee documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", completeName='" + getCompleteName() + "'" +
            ", address='" + getAddress() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", city='" + getCity() + "'" +
            ", mobile=" + getMobile() +
            ", stateEmployee='" + getStateEmployee() + "'" +
            "}";
    }
}
