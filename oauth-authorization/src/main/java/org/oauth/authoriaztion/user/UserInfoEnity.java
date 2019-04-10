package org.oauth.authoriaztion.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;



public class UserInfoEnity {

    private  String password;
    private  String username;
    private  Set<GrantedAuthority> authorities;
    private  Boolean accountNonExpired;
    private  Boolean accountNonLocked;
    private  Boolean credentialsNonExpired;
    private  Boolean enabled;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
