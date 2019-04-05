package org.oauth.authoriaztion.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WebExpressAutorityVoter implements AccessDecisionVoter<FilterInvocation> {

    private final static Log logger = LogFactory.getLog(WebExpressAutorityVoter.class);
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof   AuthorityAttr;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {

        List<String> authoritys   = extractAuthority(authentication);
        if(logger.isDebugEnabled()){
            logger.info("extractAuthority:"+String.join(",",authoritys));
        }
        List<String> needAuthAttr = attributes.stream().map(x -> x.getAttribute()).collect(Collectors.toList());
        if(logger.isDebugEnabled()){
            logger.info("needAuthAttr:"+String.join(",",needAuthAttr));
        }
        if(authoritys.containsAll(needAuthAttr)){
            return ACCESS_GRANTED;
        }else{
            return ACCESS_ABSTAIN;
        }
    }

    private List<String> extractAuthority(Authentication authentication){
       return authentication.getAuthorities().stream()
               .map(x->((GrantedAuthority) x).getAuthority())
               .collect(Collectors.toList());
    }
}
