package org.github.securityDemo.core.user;

public class GithubUserPlan implements java.io.Serializable {
    private static final long serialVersionUID = 4840806615903981692L;
    private int private_repos;
    private String name;
    private int collaborators;
    private int space;

    public int getPrivate_repos() {
        return this.private_repos;
    }

    public void setPrivate_repos(int private_repos) {
        this.private_repos = private_repos;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCollaborators() {
        return this.collaborators;
    }

    public void setCollaborators(int collaborators) {
        this.collaborators = collaborators;
    }

    public int getSpace() {
        return this.space;
    }

    public void setSpace(int space) {
        this.space = space;
    }
}
