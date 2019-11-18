package com.cscie97.store.authentication;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Tofik Mussa
 * This is an implementation of the Authentication Service API. It is also a visitable and tracked
 * by the visitor. It ia singleton class
 */
public class AuthenticationService implements IAuthenticationService, Visitable {

    private static IAuthenticationService instance;
    private List<User> users;
    private List<AuthenticationToken> tokens;
    private List<Entitlement> entitlements;
    private List<Resource> resources;
    Logger logger = Logger.getLogger(AuthenticationService.class.getName());

    /**
     * Constructor
     */
    public AuthenticationService() {
        this.users = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.entitlements = new ArrayList<>();
        this.resources = new ArrayList<>();
    }

    /**
     *
     * @return - an authentication service singleton instance
     */
    public static IAuthenticationService getInstance(){
        if(instance == null){
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Accepts contract defined by visitor
     * @param ivisitor
     */
    @Override
    public void accept(Ivisitor ivisitor) {
        ivisitor.visit(this);
    }

    /**
     *
     * @param permissionId
     * @param permissionName
     * @param permissionDescription
     * @return permission object
     */
    @Override
    public Permission createPermission(String permissionId, String permissionName, String permissionDescription) {
        Permission permission = new Permission(permissionId, permissionName, permissionDescription);
        entitlements.add(permission);
        return permission;
    }

    /**
     *
     * @param roleId
     * @param roleName
     * @param roleDescription
     * @return role object
     */
    @Override
    public Role createRole(String roleId, String roleName, String roleDescription) {
        Role role = new Role(roleId, roleName, roleDescription);
        entitlements.add(role);
        return role;
    }

    /**
     *
     * @param roleId
     * @param permissionId
     * @return role with permission added
     */
    @Override
    public Role addPermissionToRole(String roleId, String permissionId)  {
        Permission permission  = null;
        Role role = null;
        try {
            permission = getPermissionById(permissionId);
            role = getRoleById(roleId);
            role.addEntitlement(permission);
        } catch (AuthenticationServiceException e) {
            logger.warning("Adding permission to role failed for "+ e.getReason() + " : " + e.getFix());
        }
        return role;
    }

    /**
     *
     * @param parentRoleId
     * @param childRoleId
     * @return role with a child role added
     */
    @Override
    public Role addChildRoleToParentRole(String parentRoleId, String childRoleId) {
        Role parent = null;
        try {
            parent = getRoleById(parentRoleId);
            Role child = getRoleById(childRoleId);
            parent.addEntitlement(child);
        } catch (AuthenticationServiceException e) {
            logger.warning("Adding role to parent role failed for "+ e.getReason() + " : " + e.getFix());
        }
        return parent;
    }

    /**
     *
     * @param userId
     * @return a user
     */
    @Override
    public User createUser(String userId) {
        User user = new User(userId);
        users.add(user);
        return user;
    }

    /**
     *
     * @param userId
     * @param credential
     * @return a user with credentials added
     */
    @Override
    public User addCredentialsToUser(String userId, Credential credential) {
        User user = null;
        try {
             user = getUserByUserId(userId);
             user.addCredentials(credential);
        } catch (AuthenticationServiceException e) {
            logger.warning("Error finding user " + e.getReason() + " : " + e.getFix());
        }
        return user;
    }

    /**
     *
     * @param userId
     * @param entitlement
     * @return a user with entitlement added
     */
    @Override
    public User addEntitlementToUser(String userId, Entitlement entitlement) {
        User user = null;
        try {
            user = getUserByUserId(userId);
            user.addEntitlements(entitlement);
        } catch (AuthenticationServiceException e) {
            logger.warning("Error finding user " + e.getReason() + " : " + e.getFix());
        }
        return user;
    }

    /**
     *
     * @param resourceId
     * @param resourceName
     * @return a resource object
     */
    @Override
    public Resource createResource(String resourceId, String resourceName) {
        Resource resource = new Resource(resourceId, resourceName);
        resources.add(resource);
        return resource;
    }

    /**
     *
     * @param resourceRoleId
     * @param resourceRoleName
     * @param resourceRoleDesc
     * @param resourceId
     * @return a resource role object
     */
    @Override
    public ResourceRole createResourceRole(String resourceRoleId, String resourceRoleName, String resourceRoleDesc,
                                           String resourceId) {
        ResourceRole resourceRole = null;
        try {
            resourceRole = new ResourceRole(resourceRoleId, resourceRoleName, resourceRoleDesc);
            Resource resource = getResourceByResourceId(resourceId);
            resourceRole.addResources(resource);
            entitlements.add(resourceRole);
        } catch (AuthenticationServiceException e) {
            logger.warning("Error creating resource role " + e.getReason() + " : " + e.getFix());
        }
        return resourceRole;
    }

    /**
     *
     * @param resourceRoleId
     * @param entitlementId
     * @return a resource role with entitlement added
     */
    @Override
    public ResourceRole addEntitlementsToResourceRole(String resourceRoleId, String entitlementId) {
        Entitlement entitlement = null;
        ResourceRole resourceRole = null;
        try {
            resourceRole = getResourceRoleByResourceRoleId(resourceRoleId);
            entitlement = getEntitlementByEntitlementId(entitlementId);
            resourceRole.addEntitlement(entitlement);
        } catch (AuthenticationServiceException e) {
           logger.warning("Error adding entitlement " + e.getReason() + " : " + e.getFix());
        }
        return resourceRole;
    }

    /**
     *
     * @param resourceId
     * @param resourceRoleId
     * @return a resource role with resource added
     */
    @Override
    public ResourceRole addResourcesToResourceRole(String resourceId, String resourceRoleId) {
        ResourceRole resourceRole = null;
        try {
            Resource resource = getResourceByResourceId(resourceId);
            resourceRole = getResourceRoleByResourceRoleId(resourceRoleId);
            resourceRole.addResources(resource);
        } catch (AuthenticationServiceException e) {
            logger.warning("Error adding resource to resource role " + e.getReason() + " : " + e.getFix());
        }
        return resourceRole;
    }

    /**
     *
     * @param userId
     * @param resourceRoleId
     * @return a user with role added
     */
    @Override
    public User addResourceRoleToUser(String userId, String resourceRoleId) {
        User user = null;
        try {
            user = getUserByUserId(userId);
            ResourceRole resourceRole = getResourceRoleByResourceRoleId(resourceRoleId);
            user.addEntitlements(resourceRole);
        } catch (AuthenticationServiceException e) {
            logger.warning("Error adding resource role to user " + e.getReason() + " : " + e.getFix());
        }
        return user;
    }

    /**
     *
     * @param parentRoleId
     * @param childResourceRoleId
     * @return a role with child resource role added
     */
    @Override
    public Role addChildResourceRoleToParentRole(String parentRoleId, String childResourceRoleId) {
        Role parent = null;
        try {
            parent = getRoleById(parentRoleId);
            ResourceRole child = getResourceRoleByResourceRoleId(childResourceRoleId);
            parent.addEntitlement(child);
        } catch (AuthenticationServiceException e) {
            logger.warning("Error adding resource role to parent role " + e.getReason() + " : " + e.getFix());
        }
        return parent;
    }

    /**
     * Called when a user tries to log in with username and password
     * @param userId
     * @param userName
     * @param password
     * @return an authentication token
     */
    @Override
    public AuthenticationToken generateToken(String userId, String userName, String password) {
        User user = null;
        AuthenticationToken token = null;
        try {
            user = getUserByUserId(userId);
            boolean authenticated = user.checkCredentials(userName, password);
            if(authenticated){
                token = new AuthenticationToken(user);
                tokens.add(token);
            }
        } catch (AuthenticationServiceException | NoSuchAlgorithmException e) {
            logger.warning("Unable ot generate authentication token " + e.getMessage());
        }
        return token;
    }

    /**
     * Called when a user tries to log in with voice or face print
     * @param userId
     * @param voiceFacePrint
     * @return an authentication token
     */
    @Override
    public AuthenticationToken generateToken(String userId, String voiceFacePrint) {
        User user = null;
        AuthenticationToken token = null;
        try {
            user = getUserByUserId(userId);
            boolean authenticated = user.checkCredentials(voiceFacePrint);
            if(authenticated){
                token = new AuthenticationToken(user);
                tokens.add(token);
            }
        } catch (AuthenticationServiceException e) {
            logger.warning("Unable ot generate authentication token " + e.getReason() + " : " +e.getFix());
        }
        return token;
    }

    /**
     * Used internally to perform the initial check if a token exists and not expired
     * @param tokenId
     * @return an authentication token
     * @throws AccessDeniedException
     */
    @Override
    public AuthenticationToken validateIfTokenExistsAndIsValid(String tokenId) throws AccessDeniedException {
        Optional<AuthenticationToken> token = tokens.stream()
                .filter(aToken -> aToken.getTokenId().equals(tokenId))
                .findFirst();
        if(token.isEmpty() || token.get().getExpirationState() == State.EXPIRED){
            throw new AccessDeniedException("Unable to find token or token expired", "Please authenticate again");
        }
        return token.get();
    }

    /**
     * Check if a token is still valid
     * @param tokenId
     * @return token state
     */
    @Override
    public State checkTokenExpiry(String tokenId) {
        AuthenticationToken token = null;
        try {
            token = validateIfTokenExistsAndIsValid(tokenId);
        } catch (AccessDeniedException e) {
            logger.warning("Unable to check token expiry " + e.getReason() + " : " + e.getFix());
        }
        return token.getExpirationState();
    }

    /**
     * Tokens are valid for 15 minutes for the time of creation. Since a session won't last that long for the purpose of
     * the current environment, this method is designed to simulate a real life system but not applicable
     * @param tokenId
     * @return whether session timed out
     */
    @Override
    public boolean sessionTimedOut(String tokenId) {
        boolean loggedOut = false;
        AuthenticationToken token = null;
        try {
            token = validateIfTokenExistsAndIsValid(tokenId);
            token.expireToken();
            loggedOut = true;
        } catch (AccessDeniedException e) {
            logger.warning("Unable to log out for expiring session "+ e.getReason() + " : " + e.getFix());
        }
        return loggedOut;
    }

    /**
     * Called when a user logs out passing in his user id
     * @param userId
     * @return state of token
     */
    @Override
    public State logOut(String userId) {
        AuthenticationToken token = null;
        try {
            token = findValidAuthenticationTokenForAUser(userId);
            token.logOut();
        } catch (AccessDeniedException e) {
            logger.warning("Unable to log out "+ e.getReason() + " : " + e.getFix());
        }
        return token.getExpirationState();
    }

    /**
     * This method leverages the design pattern to traverse through all of the inventories in the system and
     * collects their details along the way
     * @return an accumulated print of the inventory
     */
    @Override
    public String getInventoryPrint() {
        InventoryVisitor inventoryVisitor = new InventoryVisitor();
        this.accept(inventoryVisitor);
        return inventoryVisitor.getInventoryPrint();
    }

    /**
     * Leverages the design pattern to check
     *  1 - if a token is valid
     *  2 - if the token has the required permission
     *  The permission can be global or restricted to a resource
     * @param tokenId
     * @param resource
     * @param permission
     * @throws AccessDeniedException
     */
    @Override
    public void checkAccess(String tokenId, Resource resource, Permission permission) throws AccessDeniedException {
        AuthenticationToken token = null;
        CheckAccessVisitor checkAccessVisitor = null;
        try {
             token = validateIfTokenExistsAndIsValid(tokenId);
             checkAccessVisitor = new CheckAccessVisitor(token, resource, permission);
             this.accept(checkAccessVisitor);
        } catch (AccessDeniedException e) {
            logger.info("Unable to access the resource " + e.getReason() + " : " + e.getFix());
        }
        if(!checkAccessVisitor.hasPermission()){
            throw new AccessDeniedException("Access denied",
                    "Please obtain the correct permission to access resource");
        }
    }

    /**
     * Convenience method to get users
     * @return users
     */
    @Override
    public List<User> getUsers() {
        return users;
    }

    /**
     *
     * @param roleId
     * @return a role object
     * @throws AuthenticationServiceException
     */
    @Override
    public Role getRoleById(String roleId) throws AuthenticationServiceException {
        Optional<Entitlement> entitlementRole = entitlements.stream()
                .filter(entitlement -> entitlement instanceof Role && entitlement.getEntitlementId().equals(roleId))
                .findFirst();

        if(entitlementRole.isEmpty()){
            throw new AuthenticationServiceException("Role not found",
                    "Please make sure you request the role first");
        }
        return (Role) entitlementRole.get();
    }

    /**
     *
     * @param permissionId
     * @return a role object
     * @throws AuthenticationServiceException
     */
    @Override
    public Permission getPermissionById(String permissionId) throws AuthenticationServiceException {
        Optional<Entitlement> entitlementPermission = entitlements.stream()
                .filter(entitlement -> entitlement instanceof Permission &&
                        entitlement.getEntitlementId().equals(permissionId))
                .findFirst();
        if(entitlementPermission.isEmpty()){
            throw new AuthenticationServiceException("Permission not found",
                    "Please make sure you request the permission first");
        }
        return (Permission) entitlementPermission.get();
    }

    /**
     *
     * @param userId
     * @return a user
     * @throws AuthenticationServiceException
     */
    @Override
    public User getUserByUserId(String userId) throws AuthenticationServiceException {
        Optional<User> user= users.stream()
                .filter(aUser -> aUser.getUserId().equals(userId))
                .findFirst();
        if(user.isEmpty()){
            throw new AuthenticationServiceException("User not found ", "User must register first");
        }
        return user.get();
    }

    /**
     *
     * @param resouceId
     * @return a resource
     * @throws AuthenticationServiceException
     */
    @Override
    public Resource getResourceByResourceId(String resouceId) throws AuthenticationServiceException {
        Optional<Resource> resource = resources.stream()
                .filter(Aresource -> Aresource.getResourceId().equals(resouceId))
                .findFirst();
        if(resource.isEmpty()){
            throw new AuthenticationServiceException("Resource not found",
                    "Please enter a resource that is provisioned");
        }
        return resource.get();
    }

    /**
     *
     * @param resourceRoleId
     * @return a resource role
     * @throws AuthenticationServiceException
     */
    @Override
    public ResourceRole getResourceRoleByResourceRoleId(String resourceRoleId) throws AuthenticationServiceException {

        Optional<Entitlement> entitlement = entitlements.stream()
                .filter(AresourceRole -> AresourceRole instanceof ResourceRole &&
                        AresourceRole.getEntitlementId().equals(resourceRoleId))
                .findFirst();

        if(entitlement.isEmpty()){
            throw new AuthenticationServiceException("Resource role not found",
                    "Please make sure that you have a role restricted to a resource");
        }
        return (ResourceRole) entitlement.get();
    }

    /**
     * Used internally to find if there is a valid token associated with a user
     * @param userId
     * @return a valid token associated with a user
     * @throws AccessDeniedException
     */
    @Override
    public AuthenticationToken findValidAuthenticationTokenForAUser(String userId) throws AccessDeniedException {
        Optional<AuthenticationToken> authenticationToken = tokens.stream()
                .filter(token -> token.getUser().getUserId().equals(userId))
                .findFirst();
        if(authenticationToken.isEmpty() || checkTokenExpiry(authenticationToken.get().getTokenId()) == State.EXPIRED){
            throw new AccessDeniedException("No authentication found for the user or may have timed out",
                    "Please log in again");
        }
        return authenticationToken.get();
    }

    /**
     * Gets an entitlement regards of type
     * @param entitlementId
     * @return an entitlement
     * @throws AuthenticationServiceException
     */
    @Override
    public Entitlement getEntitlementByEntitlementId(String entitlementId) throws AuthenticationServiceException {
        Optional<Entitlement> entitlement = entitlements.stream()
                .filter(Anentitlement -> Anentitlement.getEntitlementId().equals(entitlementId))
                .findFirst();
        if(entitlement.isEmpty()){
            throw new AuthenticationServiceException("Entitlement not found", "Please check the entitlement id");
        }
        return entitlement.get();
    }

    /**
     * Used internally to get tokens
     * @return tokens
     */
    public List<AuthenticationToken> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "AuthenticationService - this is the top level service containing objects :- ";
    }
}
