package org.oauth.authoriaztion.controller;

import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.securityDemo.core.token.TokenStoreUseTokenEnhancer;
import org.github.securityDemo.core.user.UserInfo;
import org.github.securityDemo.core.user.UserInfoEnity;
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


    private final static Log logger  = LogFactory.getLog(UserController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStoreUseTokenEnhancer tokenStoreUseTokenEnhancer;

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


    @RequestMapping(value="/info/detail",method =RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthorty('SCOPE_userInfo')")
    public UserInfoEnity queryUserInfoByName(){
        Map<String,Object> map = new HashMap<>(1);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken){
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt token = jwtAuthenticationToken.getToken();
            try {
                UserDetails userDetails = tokenStoreUseTokenEnhancer.getStoreAuthentication(token.getTokenValue());
                return extractOtherUserInfo((UserInfo)userDetails);
            } catch (Exception e) {
                logger.error("get userInfo error",e);
            }
        }
        throw  new UnsupportedOperationException("load detail is not org.oauth.authoriaztion.user.UserInfo.class");
    }

    private UserInfoEnity extractOtherUserInfo(UserInfo userInfo) {
        Collection<? extends GrantedAuthority> grantedAuthorities = userInfo.getAuthorities();
        Set<String> authorityStrs = grantedAuthorities
                .stream().map(x -> ((GrantedAuthority) x).getAuthority())
                .collect(Collectors.toSet());
        UserInfoEnity userInfoEnity = new UserInfoEnity();
        userInfoEnity.setUsername(userInfo.getUsername());
        userInfoEnity.setAuthorities(authorityStrs);
        userInfoEnity.setAuthorityEntities(new HashSet<>(userInfo.getBelongToRequestMap().values()));
        return userInfoEnity;
    }

    private String  extracIdToken(Jwt jwt) throws ParseException {
        String id_token = (String)jwt.getClaims().get("id_token");
        SignedJWT parse = SignedJWT.parse(id_token);
        String subject = parse.getJWTClaimsSet().getSubject();
        return  subject;
    }
}
