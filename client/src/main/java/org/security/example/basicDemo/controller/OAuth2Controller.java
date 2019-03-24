package org.security.example.basicDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    @Qualifier("oauth2ResourceRomateGetter")
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @GetMapping("/resource/get")
    public Object getResource(){
      //  System.out.println("before:"+SecurityContextHolder.getContext().getAuthentication().toString());
        ResponseEntity<Map> forEntity = oAuth2RestTemplate.getForEntity("http://127.0.0.1:9092/resource/resource/get", Map.class);
        System.out.println("after"+SecurityContextHolder.getContext().getAuthentication().toString());
        return  forEntity;
    }

    @GetMapping("/resource2/get")
    public  Object getResoucrce2(@RegisteredOAuth2AuthorizedClient("test-1")OAuth2AuthenticationToken authentication){
        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        accessToken.getTokenValue();
        System.out.println(accessToken.getTokenValue());
        return null;
    }
}
