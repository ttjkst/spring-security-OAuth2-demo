package org.security.example.basicDemo.securityConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Arrays;

@EnableOAuth2Client
@Configuration
public class OAuth2ClientConfig {

    @Value("${accessTokenUri}")
    private String accessTokenUri;
    @Value("${userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${myuserInfo}")
    private String userInfo;

    @Bean
   public OAuth2ProtectedResourceDetails resourceClientDetails(){
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails ();
      //resourceId
      details.setId("demo-test-resource");
      //clientId
      details.setClientId("demo-client");
      details.setClientSecret("secret");
      details.setAccessTokenUri(accessTokenUri);
      details.setUserAuthorizationUri(userAuthorizationUri);
      details.setScope(Arrays.asList("read", "write"));
      return details;
  }

    @Bean(name = "oauth2ResourceRomateGetter")
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(resourceClientDetails(), clientContext);
    }



    @Bean
    public ClientRegistrationRepository getClientRegistrationRepository(){
        ClientRegistration  clientRegistration = ClientRegistration.withRegistrationId("login_test")
                .clientId("my-login-client")
                .scope("read","user")
                .clientSecret("login_secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/login_test")
                .authorizationUri(userAuthorizationUri)
                .userNameAttributeName("aa")
                .tokenUri(accessTokenUri)
                .userInfoUri(userInfo)
                .jwkSetUri("http://127.0.0.1:9091/authorizan/.well-known/jwks.json")
                .build();
        InMemoryClientRegistrationRepository inMemory = new InMemoryClientRegistrationRepository(clientRegistration);
        return inMemory;
    }


    @Bean
    public OAuth2AuthorizedClientService getOAuth2AuthorizedClientService(){
        return new InMemoryOAuth2AuthorizedClientService(getClientRegistrationRepository());
    }



//    @Bean
//    public OAuth2RequestFactory oAuth2RequestFactory(){
//        OAuth2RequestFactory oAuth2RequestFactory = new DefaultOAuth2RequestFactory();
//    }
}
