package org.oauth.authoriaztion.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;

public class AuthorityResource extends DefaultFilterInvocationSecurityMetadataSource {

    private static final Log logger = LogFactory.getLog(AuthorityResource.class);

    /**
     * Sets the internal request map from the supplied map. The key elements should be of
     * type {@link RequestMatcher}, which. The path stored in the key will depend on the
     * type of the supplied UrlMatcher.
     *
     * @param requestMap order-preserving map of request definitions to attribute lists
     */
    public AuthorityResource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
        super(requestMap);
    }


//    /**
//     * Sets the internal request map from the supplied map. The key elements should be of
//     * type {@link RequestMatcher}, which. The path stored in the key will depend on the
//     * type of the supplied UrlMatcher.
//     *
//     * @param requestMap order-preserving map of request definitions to attribute lists
//     */
//    public AuthorityResource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
//        super(requestMap);
//    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Collection<ConfigAttribute> allConfigAttributes = super.getAllConfigAttributes();
        logger.info("get all configAttributs"+allConfigAttributes);
        return allConfigAttributes;
    }
}


