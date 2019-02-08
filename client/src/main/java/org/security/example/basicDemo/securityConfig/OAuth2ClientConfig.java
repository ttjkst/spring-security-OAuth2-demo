package org.security.example.basicDemo.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import java.util.Arrays;

@EnableOAuth2Client
@Configuration
public class OAuth2ClientConfig {

    @Value("${accessTokenUri}")
    private String accessTokenUri;
    @Value("${userAuthorizationUri}")
    private String userAuthorizationUri;

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


//    @Bean
//    public OAuth2RequestFactory oAuth2RequestFactory(){
//        OAuth2RequestFactory oAuth2RequestFactory = new DefaultOAuth2RequestFactory();
//    }
}
