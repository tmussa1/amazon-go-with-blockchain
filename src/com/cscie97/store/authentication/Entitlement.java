package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * A non-instantiable class that defines entitlements. Roles, ResourceRoles and Permissions are of entitlement type
 */
public abstract class Entitlement implements Visitable{

    private String entitlementId;
    private String entitlementName;
    private String entitlementDescription;

    /**
     * Constructor
     * @param entitlementId
     * @param entitlementName
     * @param entitlementDescription
     */
    public Entitlement(String entitlementId, String entitlementName, String entitlementDescription) {
        this.entitlementId = entitlementId;
        this.entitlementName = entitlementName;
        this.entitlementDescription = entitlementDescription;
    }

    public Entitlement() {
    }

    /**
     * If a resource is passed, restricted access to a resource is checked for.
     * If a resource is not passed, global access is checked for
     * @param authToken
     * @param resource
     * @param permission
     * @return permission exists or not
     */
    abstract boolean hasPermission(AuthenticationToken authToken, Resource resource, Permission permission);

    public String getEntitlementId() {
        return entitlementId;
    }
}
