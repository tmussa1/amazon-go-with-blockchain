package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * Traverses through inventories and gathers their details along the way
 */
public class InventoryVisitor implements Ivisitor {

    /**
     * Accumulates details
     */
    private StringBuilder accumulateState;

    /**
     * Marks the beginning of traversing
     */
    public InventoryVisitor() {
        this.accumulateState = new StringBuilder();
        accumulateState.append("================================================== Beginning of inventory " +
                "==================================");
    }

    /**
     * Marks the end of traversing and prints the details collected
     * @return inventory details
     */
    public String getInventoryPrint(){
        accumulateState.append("================================================== End of inventory " +
                "==================================");
        return accumulateState.toString();
    }

    /**
     * The first object to be traversed. Its children users and tokens will be traversed next
     * @param authenticationService
     */
    @Override
    public void visit(AuthenticationService authenticationService) {
        IAuthenticationService service = AuthenticationService.getInstance();
        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(service.toString());
        accumulateState.append(System.getProperty("line.separator"));

        if(service.getUsers().size() > 0){
            service.getUsers()
                    .stream()
                    .forEach(user -> user.accept(this));
        }

        if(service.getTokens().size() > 0){
            service.getTokens()
                    .stream()
                    .forEach(token -> token.accept(this));
        }
    }

    /**
     * User objects get traversed. Its children, entitlements, get traversed next
     * @param user
     */
    @Override
    public void visit(User user) {
        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(user.toString());
        accumulateState.append(System.getProperty("line.separator"));

        if(user.getEntitlements().size() > 0){
            user.getEntitlements()
                    .stream()
                    .forEach(entitlement -> entitlement.accept(this));
        }
    }

    /**
     * Role and any other child entitlements it contains get traversed
     * @param role
     */
    @Override
    public void visit(Role role) {
        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(role.toString());
        accumulateState.append(System.getProperty("line.separator"));

        if(role.getEntitlements().size() > 0){
            role.getEntitlements()
                    .stream()
                    .forEach(entitlement -> entitlement.accept(this));
        }
    }

    /**
     * Permissions get traversed. Permissions are leaf objects
     * @param permission
     */
    @Override
    public void visit(Permission permission) {
        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(permission.toString());
        accumulateState.append(System.getProperty("line.separator"));
    }

    /**
     * ResourceRoles get traversed. ResourceRoles contain child entitlements and resources which get traversed next
     * @param resourceRole
     */
    @Override
    public void visit(ResourceRole resourceRole) {
        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(resourceRole.toString());
        accumulateState.append(System.getProperty("line.separator"));

        if(resourceRole.getEntitlements().size() > 0){
            resourceRole.getEntitlements()
                    .stream()
                    .forEach(entitlement -> entitlement.accept(this));
        }

        if(resourceRole.getResources().size() > 0){
            resourceRole.getResources()
                    .stream()
                    .forEach(resource -> resource.accept(this));
        }
    }

    /**
     * Resources get traversed
     * @param resource
     */
    @Override
    public void visit(Resource resource) {
        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(resource.toString());
        accumulateState.append(System.getProperty("line.separator"));
    }

    /**
     * Tokens get traversed
     * @param authenticationToken
     */
    @Override
    public void visit(AuthenticationToken authenticationToken) {

        accumulateState.append(System.getProperty("line.separator"));
        accumulateState.append(authenticationToken.toString());
        accumulateState.append(System.getProperty("line.separator"));
    }
}
