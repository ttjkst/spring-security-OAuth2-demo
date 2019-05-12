package org.oauth.authoriaztion.securityConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.github.securityDemo.core.user.OAuth2ClientUserService;
import org.github.securityDemo.core.user.OAuth2GithubUserService;
import org.github.securityDemo.core.user.UserInfo;
import org.github.securityDemo.core.voters.FilterSecurityInterceptorObjectPostProcessor;
import org.oauth.authoriaztion.user.UserInfoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Log logger = LogFactory.getLog(WebSecurityConfig.class);

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private UserInfoDetailService userInfoDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(getTokenEndpointAuthenticationFilter(), BasicAuthenticationFilter.class)
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/**")).disable()
                .authorizeRequests()
                     .antMatchers("/login/**","/").permitAll()
                     .withObjectPostProcessor(new FilterSecurityInterceptorObjectPostProcessor())
                .and()
                    .formLogin()
                         .loginPage("/login/page")
                            .loginProcessingUrl("/login/processs")
                         .failureUrl("/login/fail").permitAll()
                .and()
                      .oauth2ResourceServer()
                         .jwt().decoder(jwtDecoder())
                      .and()
                .and()
                     .oauth2Login()
                           .userInfoEndpoint()
                              .userService(new OAuth2GithubUserService((name)->{
                                  try {
                                      return (UserInfo)userInfoDetailService.loadUserByUsername(name);
                                  }catch (Exception e){
                                      return null;
                                  }
                              }));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    public JwtDecoder jwtDecoder() throws Exception {
        return new NimbusJwtDecoderJwkSupport("http://127.0.0.1:9091/authorizan/.well-known/jwks.json");
    }



    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 用于将用户的角色映射的到scope 中
     * */
    private TokenEndpointAuthenticationFilter getTokenEndpointAuthenticationFilter() throws Exception {
        TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter  = new TokenEndpointAuthenticationFilter(this.authenticationManager(),oAuth2RequestFactory);
        return tokenEndpointAuthenticationFilter;
    }
}
