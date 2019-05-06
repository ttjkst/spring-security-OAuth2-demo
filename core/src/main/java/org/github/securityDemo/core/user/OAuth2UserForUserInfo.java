package org.github.securityDemo.core.user;

import org.github.securityDemo.core.authority.AuthorityEntity;
import org.github.securityDemo.core.utils.AuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class OAuth2UserForUserInfo implements OAuth2User {

    private final LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> belongToRequestMap;

    private final Collection<? extends GrantedAuthority> authorities;

    private final String name;


    public OAuth2UserForUserInfo(Collection<? extends GrantedAuthority> authorities, String name, Set<AuthorityEntity> authorityInfos) {
        this.authorities= authorities;
        this.name       = name;
        belongToRequestMap = new LinkedHashMap<>(authorityInfos.size());
        authorityInfos.forEach(authorityEntity -> belongToRequestMap.put(
                AuthorityUtils.mapToMatcher(authorityEntity,null)
                ,authorityEntity));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        throw new  UnsupportedOperationException("no support");
    }

    @Override
    public String getName() {
        return name;
    }


    public LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> getBelongToRequestMap() {
        return belongToRequestMap;
    }
}
