package org.security.example.basicDemo.securityConfig;

import java.io.PrintWriter;


import org.security.example.basicDemo.security.DaoFilterInvocationSecurityMetadataSource;
import org.security.example.basicDemo.security.shortCode.ShortCodeAuthenicationFilter;
import org.security.example.basicDemo.security.shortCode.ShortCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private  ShortCodeAuthenticationProvider shortCodeAuthenticationProvider;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ShortCodeAuthenicationFilter shortCodeAuthenicationFilter = new ShortCodeAuthenicationFilter();
		shortCodeAuthenicationFilter.setAuthenticationManager(this.authenticationManager());

		http.addFilterBefore(shortCodeAuthenicationFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf()
				.disable().
						authorizeRequests().anyRequest().authenticated()
                .and().authorizeRequests().antMatchers("oauth2/**").hasRole("ADMIN").and().formLogin().loginPage("/login")
				.loginProcessingUrl("/login/process/")
				.permitAll();
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
}
