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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class OAuth2GithubUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private Function<String,UserInfo> userInfoExtractor;


    public OAuth2GithubUserService(Function<String, UserInfo> userInfoExtractor) {
        this.userInfoExtractor = userInfoExtractor;
    }

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
            ResponseEntity<GithubUser> exchange = restTemplate.exchange(requestEntity, GithubUser.class);
            GithubUser githubUser            = exchange.getBody();
            if(githubUser==null){
                return  null;
            }
            UserInfo userInfo = userInfoExtractor.apply(githubUser.getName());
            Map<String,Object> extra = new HashMap<>(2);
            extra.put(OAuth2UserWithMultOAuth2Info.GITHUB_USER,githubUser);
            extra.put(OAuth2UserWithMultOAuth2Info.GITHUB_USER_ACCESS_CODE,userRequest.getAccessToken());
            return new OAuth2UserWithMultOAuth2Info(userInfo,extra);
        }catch (Exception e){
            throw new RuntimeException("can not load userInfo for authoriaztion server",e);
        }
    }
}
