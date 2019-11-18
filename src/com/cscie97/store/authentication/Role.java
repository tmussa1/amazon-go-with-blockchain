package com.cscie97.store.authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tofik Mussa
 * A role is a composite object that can contain a hierarchy of entitlements
 */
public class Role extends Entitlement implements Visitable {

    private List<Entitlement> entitlements;
    private String roleId;
    private String roleName;
    private String roleDescription;

    /**
     *
     * @param entitlementId
     * @param entitlementName
     * @param entitlementDescription
     */
    public Role(String entitlementId, String entitlementName, String entitlementDescription) {
        super(entitlementId, entitlementName, entitlementDescription);
        this.roleId = entitlementId;
        this.roleName = entitlementName;
        this.roleDescription = entitlementDescription;
        this.entitlements = new ArrayList<>();
    }

    /**
     * Adds entitlement to role
     * @param entitlement
     */
    public void addEntitlement(Entitlement entitlement){
        this.entitlements.add(entitlement);
    }

    /**
     * Role is vistable
     * @param ivisitor
     */
    @Override
    public void accept(Ivisitor ivisitor) {
        ivisitor.visit(this);
    }

    /**
     * Checks if a permission matches with it and delegates to its children if it doesn't
     * @param authToken
     * @param resource
     * @param permission
     * @return
     */
    @Override
    boolean hasPermission(AuthenticationToken authToken, Resource resource, Permission permission) {
        if(permission.getPermissionId().equals(roleId)){
            return true;
        }
        boolean hasPermission = false;
        for(Entitlement entitlement : entitlements){
            hasPermission = entitlement.hasPermission(authToken, resource, permission);
            if(hasPermission == true){
                break;
            }
        }
        return hasPermission;
    }

    public List<Entitlement> getEntitlements() {
        return entitlements;
    }

    @Override
    public String toString() {
        return "Role with role id " + roleId +
                " with role name " + roleName +
                " with description " + roleDescription;
    }
}
