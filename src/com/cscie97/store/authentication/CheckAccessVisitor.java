package com.cscie97.store.authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tofik Mussa
 * This visitor traverses through the roles, resource roles and permissions tree created using the composite pattern
 * If no permission is found, an Access Denied Exception is thrown
 */
public class CheckAccessVisitor implements Ivisitor {

    private AuthenticationToken token;
    private Resource resource;
    private Permission permission;
    private List<Boolean> hasPermission;

    /**
     *
     * @param token
     * @param resource
     * @param permission
     */
    public CheckAccessVisitor(AuthenticationToken token, Resource resource, Permission permission) {
        this.token = token;
        this.resource = resource;
        this.permission = permission;
        this.hasPermission = new ArrayList<>();
    }

    /**
     * Visits the authentication service
     * @param authenticationService
     */
    @Override
    public void visit(AuthenticationService authenticationService) {
        IAuthenticationService service = AuthenticationService.getInstance();
        service.getUsers().stream()
                .forEach(user -> user.accept(this));
    }

    /**
     * Visits users
     * @param user
     */
    @Override
    public void visit(User user) {
        user.getEntitlements().stream()
                .forEach(entitlement -> entitlement.accept(this));
    }

    /**
     * Visits roles and looks for permissions
     * @param role
     */
    @Override
    public void visit(Role role) {
        hasPermission.add(role.hasPermission(token, resource, permission));
    }

    /**
     * Visits permissions and checks if the specific permission is found
     * @param permission
     */
    @Override
    public void visit(Permission permission) {
        hasPermission.add(permission.hasPermission(token, resource, permission));
    }

    /**
     * Visits resource roles and looks for permission
     * @param resourceRole
     */
    @Override
    public void visit(ResourceRole resourceRole) {
        hasPermission.add(resourceRole.hasPermission(token, resource, permission));
    }

    /**
     * Not applicable
     * @param resource
     */
    @Override
    public void visit(Resource resource) {

    }

    /**
     * Not applicable
     * @param authenticationToken
     */
    @Override
    public void visit(AuthenticationToken authenticationToken) {

    }

    /**
     * If one of the objects contains a permission, it returns true
     * @return whether permission is found
     */
    public boolean hasPermission() {
        return hasPermission.contains(true);
    }
}
