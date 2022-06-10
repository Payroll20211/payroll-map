package co.edu.sena.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SocialSecurity.
 */
@Entity
@Table(name = "social_security")
public class SocialSecurity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "eps", length = 100, nullable = false)
    private String eps;

    @NotNull
    @Size(max = 100)
    @Column(name = "afp", length = 100, nullable = false)
    private String afp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SocialSecurity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEps() {
        return this.eps;
    }

    public SocialSecurity eps(String eps) {
        this.setEps(eps);
        return this;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getAfp() {
        return this.afp;
    }

    public SocialSecurity afp(String afp) {
        this.setAfp(afp);
        return this;
    }

    public void setAfp(String afp) {
        this.afp = afp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialSecurity)) {
            return false;
        }
        return id != null && id.equals(((SocialSecurity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialSecurity{" +
            "id=" + getId() +
            ", eps='" + getEps() + "'" +
            ", afp='" + getAfp() + "'" +
            "}";
    }
}
