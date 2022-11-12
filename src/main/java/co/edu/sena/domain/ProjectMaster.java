package co.edu.sena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ProjectMaster.
 */
@Entity
@Table(name = "project_master")
public class ProjectMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "project_master_code", length = 10, nullable = false)
    private String projectMasterCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_master_name", length = 100, nullable = false)
    private String projectMasterName;

    @NotNull
    @Size(max = 100)
    @Column(name = "cost_center_type", length = 100, nullable = false)
    private String costCenterType;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_director_name", length = 100, nullable = false)
    private String projectDirectorName;

    @NotNull
    @Size(max = 100)
    @Column(name = "phone", length = 100, nullable = false)
    private String phone;

    @ManyToOne(optional = false)
    @NotNull
    private CostCenter costCenter;

    @ManyToOne
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
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProjectMaster id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectMasterCode() {
        return this.projectMasterCode;
    }

    public ProjectMaster projectMasterCode(String projectMasterCode) {
        this.setProjectMasterCode(projectMasterCode);
        return this;
    }

    public void setProjectMasterCode(String projectMasterCode) {
        this.projectMasterCode = projectMasterCode;
    }

    public String getProjectMasterName() {
        return this.projectMasterName;
    }

    public ProjectMaster projectMasterName(String projectMasterName) {
        this.setProjectMasterName(projectMasterName);
        return this;
    }

    public void setProjectMasterName(String projectMasterName) {
        this.projectMasterName = projectMasterName;
    }

    public String getCostCenterType() {
        return this.costCenterType;
    }

    public ProjectMaster costCenterType(String costCenterType) {
        this.setCostCenterType(costCenterType);
        return this;
    }

    public void setCostCenterType(String costCenterType) {
        this.costCenterType = costCenterType;
    }

    public String getProjectDirectorName() {
        return this.projectDirectorName;
    }

    public ProjectMaster projectDirectorName(String projectDirectorName) {
        this.setProjectDirectorName(projectDirectorName);
        return this;
    }

    public void setProjectDirectorName(String projectDirectorName) {
        this.projectDirectorName = projectDirectorName;
    }

    public String getPhone() {
        return this.phone;
    }

    public ProjectMaster phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CostCenter getCostCenter() {
        return this.costCenter;
    }

    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    public ProjectMaster costCenter(CostCenter costCenter) {
        this.setCostCenter(costCenter);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ProjectMaster employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectMaster)) {
            return false;
        }
        return id != null && id.equals(((ProjectMaster) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectMaster{" +
            "id=" + getId() +
            ", projectMasterCode='" + getProjectMasterCode() + "'" +
            ", projectMasterName='" + getProjectMasterName() + "'" +
            ", costCenterType='" + getCostCenterType() + "'" +
            ", projectDirectorName='" + getProjectDirectorName() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
