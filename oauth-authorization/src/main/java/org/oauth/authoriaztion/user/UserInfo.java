package org.oauth.authoriaztion.user;

import org.oauth.authoriaztion.authority.AuthorityEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.*;

public class UserInfo implements UserDetails {


    private final String username;
    private  String password;
    private final Set<? extends GrantedAuthority> authorities;


    private final LinkedHashMap<AntPathRequestMatcher,AuthorityEntity> belongToRequestMap;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserInfo( String username,String password, Set<? extends GrantedAuthority> authorities,
                    boolean accountNonExpired, boolean accountNonLocked,
                    boolean credentialsNonExpired, boolean enabled,Set<AuthorityEntity> authorityInfos) {

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
        authorityInfos.forEach(authorityEntity -> belongToRequestMap.put(mapToMatcher(authorityEntity),authorityEntity));
        this.enabled = enabled;
    }


    private AntPathRequestMatcher mapToMatcher(AuthorityEntity authorityEntity){
        return new AntPathRequestMatcher(authorityEntity.getPath(),null,
                false,
                null);
    }

    public UserInfo( String username,String password, Set<? extends GrantedAuthority> authorities,
                    boolean accountNonExpired, boolean accountNonLocked,
                    boolean credentialsNonExpired,Set<AuthorityEntity> authorityInfos) {

        this(username,password,authorities,accountNonExpired,accountNonLocked,credentialsNonExpired,true, authorityInfos);
    }


    public UserInfo( String username,String password, Set<? extends GrantedAuthority> authorities, Set<AuthorityEntity> authorityInfos) {

        this(username,password,authorities,true,true,
                true,true, authorityInfos);
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
}
