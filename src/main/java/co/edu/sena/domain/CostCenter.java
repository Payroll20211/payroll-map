package co.edu.sena.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CostCenter.
 */
@Entity
@Table(name = "cost_center")
public class CostCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "cost_center_code", length = 10, nullable = false)
    private String costCenterCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "cost_center_name", length = 100, nullable = false)
    private String costCenterName;

    @NotNull
    @Size(max = 100)
    @Column(name = "cost_center_type", length = 100, nullable = false)
    private String costCenterType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CostCenter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCostCenterCode() {
        return this.costCenterCode;
    }

    public CostCenter costCenterCode(String costCenterCode) {
        this.setCostCenterCode(costCenterCode);
        return this;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getCostCenterName() {
        return this.costCenterName;
    }

    public CostCenter costCenterName(String costCenterName) {
        this.setCostCenterName(costCenterName);
        return this;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getCostCenterType() {
        return this.costCenterType;
    }

    public CostCenter costCenterType(String costCenterType) {
        this.setCostCenterType(costCenterType);
        return this;
    }

    public void setCostCenterType(String costCenterType) {
        this.costCenterType = costCenterType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostCenter)) {
            return false;
        }
        return id != null && id.equals(((CostCenter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostCenter{" +
            "id=" + getId() +
            ", costCenterCode='" + getCostCenterCode() + "'" +
            ", costCenterName='" + getCostCenterName() + "'" +
            ", costCenterType='" + getCostCenterType() + "'" +
            "}";
    }
}
