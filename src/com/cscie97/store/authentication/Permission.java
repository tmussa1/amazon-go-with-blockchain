package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * Permissions are leaf objects that grant access to resources
 */
public class Permission extends Entitlement implements Visitable {

    private String permissionId;
    private String permissionName;
    private String permissionDescription;

    /**
     *
     * @param entitlementId
     * @param entitlementName
     * @param entitlementDescription
     */
    public Permission(String entitlementId, String entitlementName, String entitlementDescription) {
        super(entitlementId, entitlementName, entitlementDescription);
        this.permissionId = entitlementId;
        this.permissionName = entitlementName;
        this.permissionDescription = entitlementDescription;
    }

    /**
     * Only permission id is required to make an instance of permission since the comparison is done based on just
     * permission id
     * @param permissionId
     */
    public Permission(String permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * Permission is visitable
     * @param ivisitor
     */
    @Override
    public void accept(Ivisitor ivisitor) {
        ivisitor.visit(this);
    }

    /**
     * Returns true if the permission id matches
     * @param authToken
     * @param resource
     * @param permission
     * @return whether the permission matches
     */
    @Override
    boolean hasPermission(AuthenticationToken authToken, Resource resource, Permission permission) {
        return permission.getPermissionId().equals(permissionId);
    }

    public String getPermissionId() {
        return permissionId;
    }

    @Override
    public String toString() {
        return "Permission with permission id " + permissionId +
                " with permission name " + permissionName +
                " with description " + permissionDescription;
    }
}
