package co.edu.sena.domain.enumeration;

/**
 * The StateEmployee enumeration.
 */
public enum StateEmployee {
    ACTIVE("ACTIVO"),
    INACTIVE("INACTIVO");

    private final String value;

    StateEmployee(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
