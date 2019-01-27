package org.security.example.basicDemo.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;

public class DaoFilterInvocationSecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource {

    public DaoFilterInvocationSecurityMetadataSource() {
        super(new LinkedHashMap<>(0));
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        SecurityContext context = SecurityContextHolder.getContext();
        //get Username or usermark
        Object principal = context.getAuthentication().getPrincipal();
        return SecurityConfig.createList("admin");
    }
}
