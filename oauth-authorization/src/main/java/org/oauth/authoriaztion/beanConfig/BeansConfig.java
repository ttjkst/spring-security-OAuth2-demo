package org.oauth.authoriaztion.beanConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class BeansConfig {

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;




    @Bean
    public TokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory){
        return  new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public PasswordEncoder passwordEncoderDefinded(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER","ADMIN").build());
        return manager;
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserApprovalHandler userApprovalHandler(){
        ApprovalStoreUserApprovalHandler userApprovalHandler = new ApprovalStoreUserApprovalHandler();
        userApprovalHandler.setApprovalStore(approvalStore);
        userApprovalHandler.setClientDetailsService(clientDetailsService);
        userApprovalHandler.setRequestFactory(oAuth2RequestFactory);
        return  userApprovalHandler;
    }

    @Bean
    public ApprovalStore approvalStoreBean(){
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(redisTokenStore(redisConnectionFactory));
        return store;
    }

    @Bean
    public OAuth2RequestFactory oAuth2RequestFactoryBean(){
        DefaultOAuth2RequestFactory oAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
        oAuth2RequestFactory.setCheckUserScopes(true);
        return oAuth2RequestFactory;
    }
}
