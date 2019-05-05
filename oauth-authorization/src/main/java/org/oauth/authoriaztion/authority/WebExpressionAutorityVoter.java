package org.oauth.authoriaztion.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WebExpressionAutorityVoter implements AccessDecisionVoter<FilterInvocation> {

    private final static Log logger = LogFactory.getLog(WebExpressionAutorityVoter.class);

    private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

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
            logger.debug("extractAuthority:"+String.join(",",authoritys));
        }
        List<String> needAuthAttr = extractAuthorityNeedAttrStr(attributes);
        if(logger.isDebugEnabled()){
            logger.debug("needAuthAttr:"+String.join(",",needAuthAttr));
        }
        List<String> needAttrExpressions = extractAuthorityNeedAttrExpression(attributes);
        if(logger.isDebugEnabled()){
            logger.debug("needAttrExpressions:"+String.join(",",needAttrExpressions));
        }
        int expressionAccess = ACCESS_ABSTAIN;
        for (String needAttrExpression : needAttrExpressions) {
            Expression expression = expressionHandler.getExpressionParser().parseExpression(needAttrExpression);
            EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, object);
            expressionAccess=ExpressionUtils.evaluateAsBoolean(expression, ctx) ? ACCESS_GRANTED
                    : expressionAccess;
        }

        if(authoritys.containsAll(needAuthAttr)){
            return expressionAccess==ACCESS_GRANTED?ACCESS_GRANTED:ACCESS_ABSTAIN;
        }else{
            return ACCESS_DENIED;
        }
    }

    private List<String> extractAuthority(Authentication authentication){
       return authentication.getAuthorities().stream()
               .map(x->((GrantedAuthority) x).getAuthority())
               .collect(Collectors.toList());
    }

    private List<String> extractAuthorityNeedAttrStr(Collection<ConfigAttribute> attributes){
        return attributes.stream()
                .map(x->(AuthorityAttr)x)
                .filter(x->!x.isExpression())
                .map(x->x.getAttribute()).collect(Collectors.toList());
    }

    private List<String> extractAuthorityNeedAttrExpression(Collection<ConfigAttribute> attributes){
        return attributes.stream()
                .map(x->(AuthorityAttr)x)
                .filter(x->x.isExpression())
                .map(x->x.getAttribute()).collect(Collectors.toList());
    }

}
