package org.github.securityDemo.core.user;

import org.github.securityDemo.core.utils.AuthorityUtils;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

public class OAuth2ClientUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers       = new HttpHeaders();
        headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        try {
            URI uri = UriComponentsBuilder.fromUriString(userRequest.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUri())
                    .build()
                    .toUri();
            RequestEntity<?> requestEntity         = new RequestEntity<>(headers, HttpMethod.GET, uri);
            ResponseEntity<UserInfoEnity> exchange = restTemplate.exchange(requestEntity, UserInfoEnity.class);
            UserInfoEnity userInfoEnity            = exchange.getBody();
            return new OAuth2UserForUserInfo(AuthorityUtils.packGrantedAuthoritys(userInfoEnity.getAuthorities()),userInfoEnity.getUsername(),userInfoEnity.getAuthorityEntities());
        }catch (Exception e){
            throw new RuntimeException("can not load userInfo for authoriaztion server",e);
        }
    }
}
