package co.edu.sena.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OperatorMatriz.
 */
@Entity
@Table(name = "operator_matriz")
public class OperatorMatriz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numberid")
    private Integer numberid;

    @Column(name = "digitverification")
    private Integer digitverification;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(max = 100)
    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @NotNull
    @Size(max = 100)
    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OperatorMatriz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberid() {
        return this.numberid;
    }

    public OperatorMatriz numberid(Integer numberid) {
        this.setNumberid(numberid);
        return this;
    }

    public void setNumberid(Integer numberid) {
        this.numberid = numberid;
    }

    public Integer getDigitverification() {
        return this.digitverification;
    }

    public OperatorMatriz digitverification(Integer digitverification) {
        this.setDigitverification(digitverification);
        return this;
    }

    public void setDigitverification(Integer digitverification) {
        this.digitverification = digitverification;
    }

    public String getName() {
        return this.name;
    }

    public OperatorMatriz name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public OperatorMatriz address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public OperatorMatriz city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return this.email;
    }

    public OperatorMatriz email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperatorMatriz)) {
            return false;
        }
        return id != null && id.equals(((OperatorMatriz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperatorMatriz{" +
            "id=" + getId() +
            ", numberid=" + getNumberid() +
            ", digitverification=" + getDigitverification() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
