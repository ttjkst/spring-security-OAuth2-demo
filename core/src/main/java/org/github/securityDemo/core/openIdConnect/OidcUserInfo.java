package org.github.securityDemo.core.openIdConnect;

import org.github.securityDemo.core.authority.AuthorityEntity;
import org.github.securityDemo.core.user.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Map;
import java.util.Set;

public class OidcUserInfo extends UserInfo implements OidcUser {
    public OidcUserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Set<AuthorityEntity> authorityInfos) {
        super(username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, authorityInfos);
    }

    public OidcUserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Set<AuthorityEntity> authorityInfos) {
        super(username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, authorityInfos);
    }

    public OidcUserInfo(String username, String password, Set<? extends GrantedAuthority> authorities, Set<AuthorityEntity> authorityInfos) {
        super(username, password, authorities, authorityInfos);
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public org.springframework.security.oauth2.core.oidc.OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
