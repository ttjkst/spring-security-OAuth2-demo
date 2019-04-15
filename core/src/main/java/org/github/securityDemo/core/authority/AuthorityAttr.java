package org.github.securityDemo.core.authority;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class AuthorityAttr implements ConfigAttribute {

    private String authority;

    private AntPathRequestMatcher antPathRequestMatcher;

    private boolean isExpression;
    @Override
    public String getAttribute() {
        return authority;
    }

    public AuthorityAttr(String authority, AntPathRequestMatcher antPathRequestMatcher,boolean isExpression) {
        this.authority = authority;
        this.antPathRequestMatcher = antPathRequestMatcher;
        this.isExpression = isExpression;
    }
    public boolean isExpression() {
        return isExpression;
    }

    public AntPathRequestMatcher getAntPathRequestMatcher() {
        return antPathRequestMatcher;
    }
}
