package org.security.example.basicDemo.securityConfig;

import java.io.PrintWriter;


import org.security.example.basicDemo.security.AjaxOrFormDSL;
import org.security.example.basicDemo.security.DaoFilterInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		AjaxOrFormDSL hAjaxOrFormDSL = new AjaxOrFormDSL<>().loginPage("/login")
				.loginProcessingUrl("/login/process")
				.passwordParameter("password")
				.usernameParameter("username")
				.successHandler((request, response, auth) -> {
					String contentType = request.getContentType();
					if (StringUtils.hasText(contentType)&&contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
						PrintWriter writer = response.getWriter();
						Cookie[] cookies = request.getCookies();
						for (Cookie cookie : cookies) {
							System.out.println(objectMapper.writeValueAsString(cookie));
						}
						objectMapper.writeValue(writer, auth);
						writer.flush();
					} else {
						response.sendRedirect("/success");
					}
				})
				.permitAll();
		http
				.csrf()
				.disable().
						authorizeRequests().anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<Object>() {
                    @Override
                    public Object postProcess(Object object) {
                        if(object instanceof DefaultFilterInvocationSecurityMetadataSource) {
                            return new DaoFilterInvocationSecurityMetadataSource();
                        }
                        return object;
                    }
                }).and()
                .antMatcher("/oath2/**")
                .anonymous()
                .and()
                .apply(hAjaxOrFormDSL);

				//apply(hAjaxOrFormDSL);
//				http
//				.csrf()
//				.disable()
//						.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login")
//				.loginProcessingUrl("/login/process")
//				.passwordParameter("password")
//				.usernameParameter("username")
//				.successHandler((request, response, auth) -> {
//					String contentType = request.getContentType();
//					if (StringUtils.hasText(contentType)&&contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
//						PrintWriter writer = response.getWriter();
//						objectMapper.writeValue(writer, auth);
//						writer.flush();
//					} else {
//						response.sendRedirect("/success");
//					}
//				})
//				.permitAll();
	}

	@Override
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
