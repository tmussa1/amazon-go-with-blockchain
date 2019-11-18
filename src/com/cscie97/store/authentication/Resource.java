package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * A resource object used to identify restricted access
 */
public class Resource implements Visitable {

    private String resourceId;
    private String resourceName;

    public Resource(String resourceId, String resourceName) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }

    /**
     * Resource is vistable
     * @param ivisitor
     */
    @Override
    public void accept(Ivisitor ivisitor) {
        ivisitor.visit(this);
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    @Override
    public String toString() {
        return "Resource with id " + resourceId +
                " with name " + resourceName;
    }
}
