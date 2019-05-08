package org.oauth.authoriaztion.controller;

import com.nimbusds.jwt.SignedJWT;
import org.github.securityDemo.core.authority.AuthorityEntity;
import org.github.securityDemo.core.user.UserInfo;
import org.github.ttjkst.openID.connect.store.InMemoryOpenIdConnectStore;
import org.github.ttjkst.openID.connect.store.OpenIdConnectStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private UserDetailsService userDetailsService;

    private OpenIdConnectStore openIdConnectStore = new InMemoryOpenIdConnectStore();

    @RequestMapping(value = "/info",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //@PreAuthorize("hasAuthorty('SCOPE_userInfo')")
    public Object userInfo(Principal principal){
        Map<String,Object> map = new HashMap<>(1);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken){
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt token = jwtAuthenticationToken.getToken();
            try {
                String subject = extracIdToken(token);
                UserDetails userDetails = openIdConnectStore.loadAuthenticationBySubject(subject);
            } catch (ParseException e) {

            }
        }
        return map;
    }


    @RequestMapping(value="/user/detail",method =RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthorty('SCOPE_userInfo')")
    public Map<String,Object> queryUserInfoByName(String name){
        UserDetails details = userDetailsService.loadUserByUsername(name);
        if(details==null){
            return null;
        }
        if(details instanceof UserInfo){
            UserInfo userInfo = (UserInfo) details;
            LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> requestMap = userInfo.getBelongToRequestMap();
            Map<String, Object> resultMap = extractOtherUserInfo(userInfo, requestMap);
            return resultMap;
        }
        throw  new UnsupportedOperationException("load detail is not org.oauth.authoriaztion.user.UserInfo.class");
    }

    private Map<String, Object> extractOtherUserInfo(UserInfo userInfo, LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> requestMap) {
        Collection<? extends GrantedAuthority> grantedAuthorities = userInfo.getAuthorities();
        Map<String, AuthorityEntity> requestStrMap = extractRequestMapStrs(requestMap);
        List<String> authorityStrs = grantedAuthorities.stream().map(x -> ((GrantedAuthority) x).getAuthority()).collect(Collectors.toList());

        Map<String,Object> resultMap = new HashMap<>(2);
        resultMap.put("authorityStrs",authorityStrs);
        resultMap.put("urlPaths",requestStrMap);
        return resultMap;
    }

    private Map<String, AuthorityEntity> extractRequestMapStrs(LinkedHashMap<AntPathRequestMatcher, AuthorityEntity> requestMap) {
        Map<String,AuthorityEntity>  requestStrMap = new HashMap<>(requestMap.size());
        requestMap.forEach((requestMatcher,entity)->{
            requestStrMap.put(requestMatcher.getPattern(),entity);
        });
        return requestStrMap;
    }
    private String  extracIdToken(Jwt jwt) throws ParseException {
        String id_token = (String)jwt.getClaims().get("id_token");
        SignedJWT parse = SignedJWT.parse(id_token);
        String subject = parse.getJWTClaimsSet().getSubject();
        return  subject;
    }
}
