package org.oauth.authoriaztion.beanConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.securityDemo.core.token.TokenStoreUseTokenEnhancer;
import org.oauth.authoriaztion.keyPair.KeyPairUtils;
import org.oauth.authoriaztion.user.UserInfoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;


@Configuration
public class BeansConfig {


    private static Log logger = LogFactory.getLog(BeansConfig.class);
//    @Bean
//    public TokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory){
//        return  new RedisTokenStore(redisConnectionFactory);
//    }

    @Autowired
    private KeyPair keyPair;


    @Bean
    public TokenStoreUseTokenEnhancer tokenStoreUseTokenEnhancer(){
        return new TokenStoreUseTokenEnhancer();
    }

    @Bean
    public PasswordEncoder passwordEncoderDefinded(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws IOException {
        return  new UserInfoDetailService();
    }

    @Bean
    public  KeyPair  generateKeyPair() throws NoSuchAlgorithmException {
      return KeyPairUtils.getKey();
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws NoSuchAlgorithmException {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        converter.setAccessTokenConverter(accessTokenConverter);
        return converter;
    }


}
