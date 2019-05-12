package org.github.securityDemo.core.user;

import org.github.securityDemo.core.authority.AuthorityEntity;
import org.github.securityDemo.core.utils.AuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

public class UserInfo implements UserDetails {

    private String password;
    private final String username;
    private final Set<? extends GrantedAuthority> authorities;


    private final LinkedHashMap<AntPathRequestMatcher,AuthorityEntity> belongToRequestMap;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserInfo(String username, String password, Set<? extends GrantedAuthority> authorities,
                    boolean accountNonExpired, boolean accountNonLocked,
                    boolean credentialsNonExpired, boolean enabled, Collection<AuthorityEntity> authorityInfos) {

        if (((username == null) || "".equals(username)) || (password == null)) {
            throw new IllegalArgumentException(
                    "Cannot pass null or empty values to constructor");
        }
        if (authorities==null) {
            throw new IllegalArgumentException(
                    "Cannot pass null  authorityInfos to constructor");
        }
        this.password = password;
        this.username = username;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        belongToRequestMap = new LinkedHashMap<>(authorityInfos.size());
        authorityInfos.forEach(authorityEntity -> belongToRequestMap.put(
                AuthorityUtils.mapToMatcher(authorityEntity,null)
                ,authorityEntity));
        this.enabled = enabled;
    }



    public UserInfo(String username, String password, Set<? extends GrantedAuthority> authorities,
                    boolean accountNonExpired, boolean accountNonLocked,
                    boolean credentialsNonExpired, Set<AuthorityEntity> authorityInfos) {

        this(username,password,authorities,accountNonExpired,accountNonLocked,credentialsNonExpired,true, authorityInfos);
    }


    public UserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, Set<AuthorityEntity> authorityInfos) {

        this(username,password,authorities,true,true,
                true,true, authorityInfos);
    }


    public UserInfo(UserInfo userInfo) {

        this(userInfo.getUsername(),"N/A",userInfo.authorities,userInfo.isAccountNonExpired(),userInfo.isAccountNonLocked(),
                userInfo.isCredentialsNonExpired(),userInfo.isEnabled(), userInfo.getBelongToRequestMap().values());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> getBelongToRequestMap() {
        return belongToRequestMap;
    }

    @Override
    public String toString() {
        return username;
    }
}
