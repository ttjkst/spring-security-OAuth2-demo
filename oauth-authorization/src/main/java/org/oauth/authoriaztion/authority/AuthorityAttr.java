package org.oauth.authoriaztion.authority;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

class AuthorityAttr implements ConfigAttribute {

    private String authority;

    private AntPathRequestMatcher antPathRequestMatcher;
    @Override
    public String getAttribute() {
        return authority;
    }

    public AuthorityAttr(String authority, AntPathRequestMatcher antPathRequestMatcher) {
        this.authority = authority;
        this.antPathRequestMatcher = antPathRequestMatcher;
    }

    public AntPathRequestMatcher getAntPathRequestMatcher() {
        return antPathRequestMatcher;
    }
}
