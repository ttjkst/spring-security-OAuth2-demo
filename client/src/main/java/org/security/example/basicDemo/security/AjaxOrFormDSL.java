package org.security.example.basicDemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

/**
 * Created by ttjkst on 2018/9/7.
 */
public class AjaxOrFormDSL <H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, AjaxOrFormDSL<H>, UsernamePasswordAuthenticationWithAjaxFilter> {
    private  String loginPage;

    public AjaxOrFormDSL() {
        super(new UsernamePasswordAuthenticationWithAjaxFilter(), (String)null);
        this.usernameParameter("username");
        this.passwordParameter("password");
    }

    public AjaxOrFormDSL<H> loginPage(String loginPage) {
        this.loginPage = loginPage;
        return this;
    }

    public AjaxOrFormDSL<H> usernameParameter(String usernameParameter) {
        ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).setUsernameParameter(usernameParameter);
        return this;
    }

    public AjaxOrFormDSL<H> passwordParameter(String passwordParameter) {
        ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).setPasswordParameter(passwordParameter);
        return this;
    }

    public AjaxOrFormDSL<H> failureForwardUrl(String forwardUrl) {
        this.failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
        return this;
    }

    public AjaxOrFormDSL<H> successForwardUrl(String forwardUrl) {
        this.successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
        return this;
    }
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    private String getUsernameParameter() {
        return ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).getUsernameParameter();
    }

    private String getPasswordParameter() {
        return ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).getPasswordParameter();
    }

    @Override
    public void configure(H http) throws Exception {
        super.init(http);
        this.initDefaultLoginFilter(http);
        if(StringUtils.hasText(loginPage)) {
            super.loginPage(loginPage);
        }
        ApplicationContext sharedObject = http.getSharedObject(ApplicationContext.class);
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        ObjectMapper objectMapper = sharedObject.getBean(ObjectMapper.class);
        this.getAuthenticationFilter().setObjectMapper(objectMapper);
        this.getAuthenticationFilter().setAuthenticationManager(authenticationManager);
        super.configure(http);
    }

    private void initDefaultLoginFilter(H http) {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = (DefaultLoginPageGeneratingFilter)http.getSharedObject(DefaultLoginPageGeneratingFilter.class);
        if(loginPageGeneratingFilter != null && !this.isCustomLoginPage()) {
            loginPageGeneratingFilter.setFormLoginEnabled(true);
            loginPageGeneratingFilter.setUsernameParameter(this.getUsernameParameter());
            loginPageGeneratingFilter.setPasswordParameter(this.getPasswordParameter());
            loginPageGeneratingFilter.setLoginPageUrl(this.getLoginPage());
            loginPageGeneratingFilter.setFailureUrl(this.getFailureUrl());
            loginPageGeneratingFilter.setAuthenticationUrl(this.getLoginProcessingUrl());
        }

    }
}
