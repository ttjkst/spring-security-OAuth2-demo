package org.aouth.resource.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@EnableResourceServer
@Configuration
public class OAuth2ServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;


    @Value("${resource.jwt.jwkSetUrl}")
    private String jwtUrls;
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("demo-test-resource");
        resources.tokenStore(getTokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().requestMatchers()
                .antMatchers("/resource/**")
                .and()
                .authorizeRequests()
                .antMatchers("/resource/**")
                .access("#oauth2.hasScope('read')")
                .and().oauth2ResourceServer().jwt().decoder(jwtDecoder());
    }


    @Bean
    JwtDecoder jwtDecoder() throws Exception {
       return new NimbusJwtDecoderJwkSupport(jwtUrls);
    }


    @Bean
    public TokenStore getTokenStore(){
        return new JwkTokenStore("http://127.0.0.1:9091/authorizan/.well-known/jwks.json");
    }
    private RSAPublicKey key() throws Exception {
        String encoded = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdlatRjRjogo3WojgGHFHYLugd" +
                "UWAY9iR3fy4arWNA1KoS8kVw33cJibXr8bvwUAUparCwlvdbH6dvEOfou0/gCFQs" +
                "HUfQrSDv+MuSUMAe8jzKE4qW+jK+xQU9a03GUnKHkkle+Q0pX/g6jXZ7r1/xAK5D" +
                "o2kQ+X5xK9cipRgEKwIDAQAB";
        byte[] bytes = Base64.getDecoder().decode(encoded.getBytes());
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(bytes));
    }

}
