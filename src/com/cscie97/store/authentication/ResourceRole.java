package com.cscie97.store.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Tofik Mussa
 * A resource role is a special type of role which ties permissions to a specific resource. It can have children
 * entitlements
 */
public class ResourceRole extends Role implements Visitable {

    private List<Entitlement> entitlements;
    private List<Resource> resources;
    private String resourceRoleId;
    private String resourceRoleName;
    private String resourceRoleDescrption;

    /**
     *
     * @param entitlementId
     * @param entitlementName
     * @param entitlementDescription
     */
    public ResourceRole(String entitlementId, String entitlementName, String entitlementDescription) {
        super(entitlementId, entitlementName, entitlementDescription);
        this.resourceRoleId = entitlementId;
        this.resourceRoleName = entitlementName;
        this.resourceRoleDescrption = entitlementDescription;
        this.entitlements = new ArrayList<>();
        this.resources = new ArrayList<>();
    }

    /**
     * Adds entitlements to resource roles
     * @param entitlement
     */
    @Override
    public void addEntitlement(Entitlement entitlement){
        this.entitlements.add(entitlement);
    }

    /**
     * Adds resources to resource roles
     * @param resource
     */
    public void addResources(Resource resource){
        this.resources.add(resource);
    }

    /**
     * ResourceRole is visitable
     * @param ivisitor
     */
    @Override
    public void accept(Ivisitor ivisitor) {
        ivisitor.visit(this);
    }

    /**
     * Checks if the permissions match and delegates to its children if it doesn't
     * @param authToken
     * @param resource
     * @param permission
     * @return whether permission is found
     */
    @Override
    boolean hasPermission(AuthenticationToken authToken, Resource resource, Permission permission) {
        if( (resource != null && checkResource(resource)) || permission.getPermissionId().equals(resourceRoleId)){
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

    /**
     * Checks if resources match with the ones tied to a ResourceRole
     * @param resource
     * @return
     */
    boolean checkResource(Resource resource){
        Optional<Resource> resourceFound = resources.stream()
                .filter(aResource -> aResource.getResourceId().equals(resource.getResourceId()))
                .findFirst();
        if(!resourceFound.isEmpty()){
            return true;
        }
        return false;
    }
    @Override
    public List<Entitlement> getEntitlements() {
        return entitlements;
    }

    public List<Resource> getResources() {
        return resources;
    }

    @Override
    public String toString() {
        return "ResourceRole with id " + resourceRoleId +
                " with name " + resourceRoleName +
                " with descrption " + resourceRoleDescrption;
    }
}
