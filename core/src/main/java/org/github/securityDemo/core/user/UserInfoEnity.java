package org.github.securityDemo.core.user;

import org.github.securityDemo.core.authority.AuthorityEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;


public class UserInfoEnity implements Serializable {

    private  String password;
    private  String username;
    private  Set<String> authorities;
    private  Boolean accountNonExpired;
    private  Boolean accountNonLocked;
    private  Boolean credentialsNonExpired;
    private  Boolean enabled;
    private  Set<AuthorityEntity> authorityEntities;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Set<AuthorityEntity> getAuthorityEntities() {
        return authorityEntities==null?Collections.emptySet():authorityEntities;
    }

    public void setAuthorityEntities(Set<AuthorityEntity> authorityEntities) {
        this.authorityEntities = authorityEntities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getAuthorities() {
        return authorities==null? Collections.emptySet():authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
