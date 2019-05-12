package org.oauth.authoriaztion.securityConfig;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.securityDemo.core.token.TokenStoreUseTokenEnhancer;
import org.github.ttjkst.openID.connect.token.OpenIdConnectTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final Log logger = LogFactory.getLog(AuthorizationServerConfiguration.class);
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    @Qualifier(BeanIds.AUTHENTICATION_MANAGER)
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private  KeyPair keyPair;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;


    @Autowired
    private TokenStoreUseTokenEnhancer tokenStoreUseTokenEnhancer;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                    .withClient("demo-client")
                        .resourceIds("demo-test-resource")
                        .authorizedGrantTypes("authorization_code")
                        .authorities("OAUTH2_CLIENT")
                        .scopes("read","write")
                        .secret("secret")
                        .redirectUris("http://localhost:9093/client/oauth2/resource/get")
                .and()
                    .withClient("my-login-client")
                    .resourceIds("asasas-1")
                    .authorizedGrantTypes("authorization_code")
                    .authorities("OAUTH2_CLIENT")
                    .scopes("read","user")
                    .secret("login_secret")
                    ///至少要配置一个
                    .redirectUris("http://localhost:9093/client/oauth2/resource/get",
                            "http://localhost:9093/client/login/oauth2/code/login_test");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        OpenIdConnectTokenEnhancer tokenEnhancer = new OpenIdConnectTokenEnhancer();
        tokenEnhancer.setKeyPair(keyPair);
        endpoints.tokenStore(getTokenStore())
                .authenticationManager(authenticationManager)
                .userApprovalHandler(userApprovalHandler)
                .accessTokenConverter(accessTokenConverter);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer,accessTokenConverter,tokenStoreUseTokenEnhancer));
        endpoints.tokenEnhancer(tokenEnhancerChain);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
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
    public ApprovalStore approvalStoreBean() throws NoSuchAlgorithmException {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(getTokenStore());
        return store;
    }

    @Bean
    public OAuth2RequestFactory oAuth2RequestFactoryBean(){
        DefaultOAuth2RequestFactory oAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
        oAuth2RequestFactory.setCheckUserScopes(true);
        return oAuth2RequestFactory;
    }

    @Bean
    public TokenStore getTokenStore() throws NoSuchAlgorithmException {
        return new JwtTokenStore(accessTokenConverter);
    }


    @FrameworkEndpoint
    class JwkSetEndpoint {
        @Autowired
        KeyPair keyPair;

        public JwkSetEndpoint(KeyPair keyPair) {
            this.keyPair = keyPair;
        }

        @GetMapping("/.well-known/jwks.json")
        @ResponseBody
        public Map<String, Object> getKey(Principal principal) {
            RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
            com.nimbusds.jose.jwk.RSAKey key = new RSAKey.Builder(publicKey).build();
            logger.info( "send public key"+publicKey.toString());
            return new JWKSet(key).toJSONObject();
        }
    }
}
