package org.github.securityDemo.core.utils;

import org.github.securityDemo.core.authority.AuthorityEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface AuthorityUtils {


    static AntPathRequestMatcher mapToMatcher(AuthorityEntity authorityEntity, UrlPathHelper urlPathHelper){
        return new AntPathRequestMatcher(authorityEntity.getPath(),authorityEntity.getHttpMethod(),
                authorityEntity.getCaseSensitive(),
                urlPathHelper);
    }

    static Set<SimpleGrantedAuthority> packGrantedAuthoritys(Collection<String> authoritys){
       return authoritys.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
