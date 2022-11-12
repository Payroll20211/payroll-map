package co.edu.sena.domain;

import co.edu.sena.domain.enumeration.StateDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DocumentType.
 */
@Entity
@Table(name = "document_type")
public class DocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "document_name", length = 50, nullable = false, unique = true)
    private String documentName;

    @NotNull
    @Size(max = 10)
    @Column(name = "initials", length = 10, nullable = false, unique = true)
    private String initials;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state_document_type", nullable = false)
    private StateDocument stateDocumentType;

    @OneToMany(mappedBy = "documentType")
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
    private Set<Employee> documentTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public DocumentType documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getInitials() {
        return this.initials;
    }

    public DocumentType initials(String initials) {
        this.setInitials(initials);
        return this;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public StateDocument getStateDocumentType() {
        return this.stateDocumentType;
    }

    public DocumentType stateDocumentType(StateDocument stateDocumentType) {
        this.setStateDocumentType(stateDocumentType);
        return this;
    }

    public void setStateDocumentType(StateDocument stateDocumentType) {
        this.stateDocumentType = stateDocumentType;
    }

    public Set<Employee> getDocumentTypes() {
        return this.documentTypes;
    }

    public void setDocumentTypes(Set<Employee> employees) {
        if (this.documentTypes != null) {
            this.documentTypes.forEach(i -> i.setDocumentType(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setDocumentType(this));
        }
        this.documentTypes = employees;
    }

    public DocumentType documentTypes(Set<Employee> employees) {
        this.setDocumentTypes(employees);
        return this;
    }

    public DocumentType addDocumentType(Employee employee) {
        this.documentTypes.add(employee);
        employee.setDocumentType(this);
        return this;
    }

    public DocumentType removeDocumentType(Employee employee) {
        this.documentTypes.remove(employee);
        employee.setDocumentType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return id != null && id.equals(((DocumentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", initials='" + getInitials() + "'" +
            ", stateDocumentType='" + getStateDocumentType() + "'" +
            "}";
    }
}
