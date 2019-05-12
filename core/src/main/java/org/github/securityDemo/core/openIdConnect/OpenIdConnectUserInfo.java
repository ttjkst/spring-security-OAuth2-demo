package org.github.securityDemo.core.openIdConnect;

import org.github.securityDemo.core.authority.AuthorityEntity;
import org.github.securityDemo.core.user.UserInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class OpenIdConnectUserInfo extends UserInfo {
    public OpenIdConnectUserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Set<AuthorityEntity> authorityInfos) {
        super(username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, authorityInfos);
    }

    public OpenIdConnectUserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Set<AuthorityEntity> authorityInfos) {
        super(username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, authorityInfos);
    }

    public OpenIdConnectUserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, Set<AuthorityEntity> authorityInfos) {
        super(username, password, authorities, authorityInfos);
    }
}
