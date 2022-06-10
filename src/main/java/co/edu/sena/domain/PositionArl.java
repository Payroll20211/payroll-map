package co.edu.sena.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PositionArl.
 */
@Entity
@Table(name = "position_arl")
public class PositionArl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "risk_class", nullable = false)
    private Integer riskClass;

    @NotNull
    @Size(max = 10)
    @Column(name = "position_code", length = 10, nullable = false)
    private String positionCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "position", length = 100, nullable = false)
    private String position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PositionArl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRiskClass() {
        return this.riskClass;
    }

    public PositionArl riskClass(Integer riskClass) {
        this.setRiskClass(riskClass);
        return this;
    }

    public void setRiskClass(Integer riskClass) {
        this.riskClass = riskClass;
    }

    public String getPositionCode() {
        return this.positionCode;
    }

    public PositionArl positionCode(String positionCode) {
        this.setPositionCode(positionCode);
        return this;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPosition() {
        return this.position;
    }

    public PositionArl position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionArl)) {
            return false;
        }
        return id != null && id.equals(((PositionArl) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionArl{" +
            "id=" + getId() +
            ", riskClass=" + getRiskClass() +
            ", positionCode='" + getPositionCode() + "'" +
            ", position='" + getPosition() + "'" +
            "}";
    }
}
