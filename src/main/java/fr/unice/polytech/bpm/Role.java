package fr.unice.polytech.bpm;

/**
 * A process user role
 */
public enum Role {
    SUPERVISOR("superviseur"), LOCATION_MANAGER("responsable lieu"), UNKNOWN("");

    private String name;

    Role(String name) {
        this.name = name;
    }

    /**
     *
     * @param name
     * @return a role from its name
     */
    public static Role fromName(String name) {
        for (Role role : Role.values()) {
            if (role.name.equals(name)) {
                return role;
            }
        }
        return UNKNOWN;
    }
}
