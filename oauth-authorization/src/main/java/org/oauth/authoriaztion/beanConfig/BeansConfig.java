package org.oauth.authoriaztion.beanConfig;

import org.oauth.authoriaztion.user.UserInfoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.IOException;


@Configuration
public class BeansConfig {

//    @Bean
//    public TokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory){
//        return  new RedisTokenStore(redisConnectionFactory);
//    }

    @Bean
    public PasswordEncoder passwordEncoderDefinded(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws IOException {
        return  new UserInfoDetailService();
    }
}
