package org.security.example.basicDemo.securityConfig;

import org.security.example.basicDemo.security.UserDetailsServicePlus;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class UserDetailsConfig implements  WebMvcConfigurer{


	@Bean
	public UserDetailsService userDetailsService(){
		return new UserDetailsServicePlus();
	}

}
