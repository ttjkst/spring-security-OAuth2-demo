package org.oauth.authoriaztion.securityConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oauth.authoriaztion.authority.AuthorityResource;
import org.oauth.authoriaztion.authority.WebExpressionAutorityVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.*;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Log logger = LogFactory.getLog(WebSecurityConfig.class);

	@Autowired
	private OAuth2RequestFactory oAuth2RequestFactory;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(getTokenEndpointAuthenticationFilter(), BasicAuthenticationFilter.class)
					.csrf()
					.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/**"))
					.disable()
					.authorizeRequests()
				    .antMatchers("/login/**","/")
					.permitAll()
					.withObjectPostProcessor(new FilterSecurityInterceptorObjectPostProcessor())
				.and()
					.formLogin()
				    	//.loginPage("/login")
						.loginProcessingUrl("/login/processs")
						.failureUrl("/login/fail")
				    	.permitAll()
				.and()
					.oauth2ResourceServer()
					.jwt()
					.decoder(jwtDecoder())
				.and();
////				// default protection for all resources (including /oauth/authorize)
//					.authorizeRequests()
//				    	.mvcMatchers("/.well-known/jwks.json").
//				    permitAll()
//						.mvcMatchers("/user/info")
//					.hasAuthority("SCOPE_userInfo")
//						.anyRequest()
//					.hasAnyRole("ADMIN","USER");
//				.and().oauth2ResourceServer().jwt().decoder(jwtDecoder());
		// ... more configuration, e.g. for form login
	}

	public JwtDecoder jwtDecoder() throws Exception {
		return new NimbusJwtDecoderJwkSupport("http://127.0.0.1:9091/authorizan/.well-known/jwks.json");
	}



	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	private static final class FilterSecurityInterceptorObjectPostProcessor implements
			ObjectPostProcessor<Object> {
		private List<ObjectPostProcessor<? extends Object>> postProcessors = new ArrayList<ObjectPostProcessor<?>>();

		@SuppressWarnings({"rawtypes", "unchecked"})
		public Object postProcess(Object object) {
			if (object == null) {
				logger.info("reset securityMetadataSource but class not fit and object is null,class is "+object.getClass());
				return null;
			}
			if (FilterSecurityInterceptor.class.isAssignableFrom(object.getClass())) {
				logger.info("reset securityMetadataSource find it");
				FilterSecurityInterceptor securityInterceptor = (FilterSecurityInterceptor) object;

//                try {
//                    securityInterceptor.setSecurityMetadataSource(new AuthorityResource());
//                } catch (IOException e) {
//                    throw new RuntimeException("init AuthorityResource fail",e);
//                }
                AccessDecisionManager accessDecisionManager = securityInterceptor.getAccessDecisionManager();
                if(accessDecisionManager instanceof AbstractAccessDecisionManager){
                    ((AbstractAccessDecisionManager) accessDecisionManager).getDecisionVoters().add(new WebExpressionAutorityVoter());
                }
            }else {
				logger.info("reset securityMetadataSource but class not fit,class is " + object.getClass());
			}
			return object;
		}


//		private void setFilterSecurityInvoianResource(FilterSecurityInterceptor filterSecurityInterceptor) {
//
//			RequestMatcher user = new AntPathRequestMatcher("/user/info");
//			RequestMatcher wellKnow = new AntPathRequestMatcher("/.well-known/jwks.json");
//			RequestMatcher all      = new AntPathRequestMatcher("/**");
//			List<ConfigAttribute> hasSocpe_userInfo = SecurityConfig.createList("hasAuthority('SCOPE_userInfo')");
//			List<ConfigAttribute> hasAnyRole        = SecurityConfig.createList("hasAnyRole('ROLE_ADMIN','ROLE_USER')");
//			List<ConfigAttribute> permitAll         = SecurityConfig.createList("permitAll");
//			LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> matcherListMap = new LinkedHashMap<>(3);
//			matcherListMap.put(user,hasSocpe_userInfo);
//			matcherListMap.put(wellKnow,permitAll);
//			matcherListMap.put(all,hasAnyRole);
//			ExpressionBasedFilterInvocationSecurityMetadataSource resource = new
//					ExpressionBasedFilterInvocationSecurityMetadataSource(matcherListMap,
//					new DefaultWebSecurityExpressionHandler());
//
//			filterSecurityInterceptor.setSecurityMetadataSource(resource);
//		}
	}



	/**
	 * 用于将用户的角色映射的到scope 中
	 * */
	private TokenEndpointAuthenticationFilter getTokenEndpointAuthenticationFilter() throws Exception {
		TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter  = new TokenEndpointAuthenticationFilter(this.authenticationManager(),oAuth2RequestFactory);
		return tokenEndpointAuthenticationFilter;
	}
}
