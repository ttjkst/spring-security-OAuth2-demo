package org.security.example.basicDemo.securityConfig;

import org.security.example.basicDemo.security.UserDetailsServicePlus;
import org.security.example.basicDemo.security.shortCode.ShortCodeDetailService;
import org.security.example.basicDemo.security.shortCode.userDetailService.InMemoryShortCodeDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class UserDetailsConfig implements  WebMvcConfigurer{


	@Bean
	public UserDetailsService userDetailsService(){
		return new UserDetailsServicePlus();
	}

	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public ShortCodeDetailService shortCodeDetailService() {
		InMemoryShortCodeDetailsService inMemoryShortCodeDetailsService = new InMemoryShortCodeDetailsService();
		inMemoryShortCodeDetailsService.addUserDetails("test_1", User.withUsername("admin").password(passwordEncoder().encode("admin"))
				.authorities("ROLE_ADMIN")
				.build());

		return inMemoryShortCodeDetailsService;
	}

}
