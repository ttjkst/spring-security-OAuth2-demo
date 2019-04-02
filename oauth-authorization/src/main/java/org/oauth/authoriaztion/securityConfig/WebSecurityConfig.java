package org.oauth.authoriaztion.securityConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oauth.authoriaztion.authority.AuthorityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.GenericTypeResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.security.Security;
import java.util.*;

@EnableWebSecurity
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
					.authorizeRequests().antMatchers("/login/**","/")
					.permitAll()
					.withObjectPostProcessor(new FilterSecurityInterceptorObjectPostProcessor())
				.and()
					.formLogin()
					.loginProcessingUrl("/login/processs")
					.failureUrl("/login/fail")
				.and()
					.oauth2ResourceServer()
					.jwt()
					.decoder(jwtDecoder());
//				// default protection for all resources (including /oauth/authorize)
//					.authorizeRequests()
//				    	.mvcMatchers("/.well-known/jwks.json").
//				    permitAll()
//						.mvcMatchers("/user/info")
//					.hasAuthority("SCOPE_userInfo")
//						.anyRequest()
//					.hasAnyRole("ADMIN","USER")
//				.and().oauth2ResourceServer().jwt().decoder(jwtDecoder());
		// ... more configuration, e.g. for form login
	}

	public JwtDecoder jwtDecoder() throws Exception {
		return new NimbusJwtDecoderJwkSupport("http://127.0.0.1:9091/authorizan/.well-known/jwks.json");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.objectPostProcessor(new FilterSecurityInterceptorObjectPostProcessor());
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
				setFilterSecurityInvoianResource(securityInterceptor);
			}else {
				logger.info("reset securityMetadataSource but class not fit,class is " + object.getClass());
			}
			return object;
		}


		private void setFilterSecurityInvoianResource(FilterSecurityInterceptor filterSecurityInterceptor) {

			RequestMatcher userInfo = new AntPathRequestMatcher("/user/info");
			RequestMatcher wellKnow = new AntPathRequestMatcher("/.well-known/jwks.json");
			RequestMatcher all      = new AntPathRequestMatcher("/**");
			List<ConfigAttribute> hasSocpe_userInfo = SecurityConfig.createList("hasAuthority('SCOPE_userInfo')");
			List<ConfigAttribute> hasAnyRole        = SecurityConfig.createList("hasAnyRole('ROLE_ADMIN','ROLE_USER')");
			List<ConfigAttribute> permitAll         = SecurityConfig.createList("permitAll");
			LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> matcherListMap = new LinkedHashMap<>(3);
			matcherListMap.put(userInfo,hasSocpe_userInfo);
			matcherListMap.put(wellKnow,permitAll);
			matcherListMap.put(all,hasAnyRole);
			ExpressionBasedFilterInvocationSecurityMetadataSource resource = new
					ExpressionBasedFilterInvocationSecurityMetadataSource(matcherListMap,
					new DefaultWebSecurityExpressionHandler());

			filterSecurityInterceptor.setSecurityMetadataSource(resource);
		}
	}



		private TokenEndpointAuthenticationFilter getTokenEndpointAuthenticationFilter() throws Exception {
		TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter  = new TokenEndpointAuthenticationFilter(this.authenticationManager(),oAuth2RequestFactory);
		return tokenEndpointAuthenticationFilter;
	}
}
