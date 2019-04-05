package org.oauth.authoriaztion.authority;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class AuthorityResource implements FilterInvocationSecurityMetadataSource {



    private static final Log logger = LogFactory.getLog(AuthorityResource.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private LinkedList<AuthorityAttr> authorityAttrs;

    public AuthorityResource() throws IOException {

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionLikeType(List.class,AuthorityEntity.class);
        Collection<AuthorityEntity> authorityEntities = (Collection<AuthorityEntity>) objectMapper.readValue(new File(
                "D:\\javaWorkplace\\spring-security-OAuth2-demo\\oauth-authorization\\src\\main\\java\\org\\" +
                        "oauth\\authoriaztion\\authority\\dataAuthorityJSON.json"), javaType);
        authorityAttrs = mapToAuthorityAttrs(authorityEntities);
    }

    private LinkedList<AuthorityAttr> mapToAuthorityAttrs(Collection<AuthorityEntity> authorityEntities){
        LinkedList<AuthorityAttr> linkedList= new LinkedList<AuthorityAttr>();
        authorityEntities.forEach(entity->{
            AntPathRequestMatcher antPath = new AntPathRequestMatcher(entity.getPath(),null,
                    false,
                    null);
            AuthorityAttr attr = new AuthorityAttr(entity.getAuthority(),antPath);
            linkedList.add(attr);
        });
        return linkedList;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
       if(object instanceof FilterInvocation){
           FilterInvocation filterInvocation = (FilterInvocation) object;
           return authorityAttrs.stream()
                   .filter(x -> x.getAntPathRequestMatcher().matches(filterInvocation.getRequest()))
                   .collect(Collectors.toList());
       }else{
           throw new  UnsupportedOperationException(" unsported this class"+object!=null?object.getClass().toString():null);
       }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


}


