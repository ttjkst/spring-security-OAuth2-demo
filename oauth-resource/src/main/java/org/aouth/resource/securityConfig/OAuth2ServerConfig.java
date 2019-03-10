package org.aouth.resource.securityConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@EnableWebSecurity
@Configuration
public class OAuth2ServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${resource.jwt.jwkSetUrl}")
    private String jwtUrls;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().requestMatchers()
                .antMatchers("/resource/**")
                .and()
                .authorizeRequests()
                .antMatchers("/resource/**")
                .hasAuthority("SCOPE_read")
                .and().oauth2ResourceServer()
                .jwt()
                .decoder(jwtDecoder());
    }


    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
       return new NimbusJwtDecoderJwkSupport(jwtUrls);
    }


//    @Bean
//    public TokenStore getTokenStore(){
//        JwtTokenStore jwkTokenStore = new JwtTokenStore(new JwtAccessTokenConverter());
//        return jwkTokenStore;
//    }
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
