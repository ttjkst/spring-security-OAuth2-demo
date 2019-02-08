package org.oauth.authoriaztion.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	private OAuth2RequestFactory oAuth2RequestFactory;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(getTokenEndpointAuthenticationFilter(), BasicAuthenticationFilter.class)
				.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/**"))
				.disable().authorizeRequests().antMatchers("/login/**","/")
				.permitAll()
				.and()
				.formLogin().loginProcessingUrl("/login/processs").failureUrl("/login/fail")
				.and()
				// default protection for all resources (including /oauth/authorize)
				.authorizeRequests()
				.anyRequest()
				.hasAnyRole("ADMIN","USER");
		// ... more configuration, e.g. for form login
	}

	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	private TokenEndpointAuthenticationFilter getTokenEndpointAuthenticationFilter() throws Exception {
		TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter  = new TokenEndpointAuthenticationFilter(this.authenticationManager(),oAuth2RequestFactory);
		return tokenEndpointAuthenticationFilter;
	}
}
