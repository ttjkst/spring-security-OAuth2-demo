package org.github.securityDemo.core.voters;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class FilterSecurityInterceptorObjectPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {

    @Override
    public FilterSecurityInterceptor postProcess(FilterSecurityInterceptor securityInterceptor) {
            AccessDecisionManager accessDecisionManager = securityInterceptor.getAccessDecisionManager();
            if(accessDecisionManager instanceof AbstractAccessDecisionManager){
                ((AbstractAccessDecisionManager) accessDecisionManager).getDecisionVoters().add(new UserInfoWebExpresssionAuthorityVoter());
            }

        return securityInterceptor;
    }

}
