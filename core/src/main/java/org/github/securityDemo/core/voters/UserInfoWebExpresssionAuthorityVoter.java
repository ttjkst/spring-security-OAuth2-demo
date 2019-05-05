package org.github.securityDemo.core.voters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.securityDemo.core.authority.AuthorityEntity;
import org.github.securityDemo.core.user.UserInfo;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

public class UserInfoWebExpresssionAuthorityVoter implements AccessDecisionVoter<FilterInvocation> {

    private final static Log logger = LogFactory.getLog(UserInfoWebExpresssionAuthorityVoter.class);

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        HttpServletRequest request = object.getRequest();
        //only for  org.github.securityDemo.core.user.UserInfo
        if(authentication.getPrincipal() instanceof UserInfo){
            UserInfo currUserInfo = (UserInfo)authentication.getPrincipal();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> belongToRequestMap = currUserInfo.getBelongToRequestMap();
            Set<AntPathRequestMatcher> requestMatchers = belongToRequestMap.keySet();
            for (AntPathRequestMatcher requestMatcher : requestMatchers) {
                //match current request
                if(requestMatcher.matches(request)){
                    AuthorityEntity authorityEntity = belongToRequestMap.get(requestMatcher);
                    //if any  granted
                    if(authorityEntity.getAny()){
                        return ACCESS_GRANTED;
                    }
                    //
                    if(authorities.contains(authorityEntity.getAuthority())){
                        return ACCESS_GRANTED;
                    }
                }
            }
            return ACCESS_DENIED;
        }else{
            //弃权
            logger.info("org.oauth.authoriaztion.org.github.securityDemo.core.authority.UserInfoWebExpresssionAuthorityVoter is ACCESS_ABSTAIN ");
            return  ACCESS_ABSTAIN;
        }
    }
}
