package org.security.example.basicDemo.securityConfig;

import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.github.securityDemo.core.user.OAuth2ClientUserService;
import org.github.securityDemo.core.user.UserInfoEnity;
import org.github.securityDemo.core.utils.AuthorityUtils;
import org.github.securityDemo.core.voters.FilterSecurityInterceptorObjectPostProcessor;
import org.security.example.basicDemo.security.shortCode.ShortCodeAuthenicationFilter;
import org.security.example.basicDemo.security.shortCode.ShortCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private  ShortCodeAuthenticationProvider shortCodeAuthenticationProvider;

	@Autowired
	private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/**
		 * 另一种身份认证（非权限校验）
		 * */
		ShortCodeAuthenicationFilter shortCodeAuthenicationFilter = new ShortCodeAuthenicationFilter();
		shortCodeAuthenicationFilter.setAuthenticationManager(this.authenticationManager());

		http.addFilterBefore(shortCodeAuthenicationFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf()
				.disable()
						.authorizeRequests()
						.anyRequest()
						.authenticated()
                .and()
					.authorizeRequests().withObjectPostProcessor(new FilterSecurityInterceptorObjectPostProcessor())
					.antMatchers("oauth2/**")
					.hasRole("ADMIN")
				    .antMatchers("/OAuth/login/**","/OAuth/login/process/**","/login/end/fail")
				    .permitAll()
				.and()
					.oauth2Login()
				        .loginPage("/login/end/fail")
				   			.authorizationEndpoint()
								.baseUri("/OAuth/login/process")
						.and()
							.userInfoEndpoint()
								.userService(new OAuth2ClientUserService())
				        .and()
							.defaultSuccessUrl("/login/end/success")
				        .permitAll()
				.and()
					.oauth2Client()
					.authorizedClientService(oAuth2AuthorizedClientService)
					.authorizationCodeGrant()
						.authorizationRequestRepository(this.cookieAuthorizationRequestRepository());
	}
	@Override
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(shortCodeAuthenticationProvider);
	}

	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository ();
	}


}
