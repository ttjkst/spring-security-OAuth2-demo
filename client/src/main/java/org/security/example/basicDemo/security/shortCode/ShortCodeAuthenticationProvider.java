package org.security.example.basicDemo.security.shortCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class ShortCodeAuthenticationProvider implements AuthenticationProvider, InitializingBean {


    private final Log logger = LogFactory.getLog(ShortCodeAuthenticationProvider.class);

    private ShortCodeDetailService shortCodeDetailService;

    private PasswordEncoder passwordEncoder;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ShortCodeAuthenticationToken token = (ShortCodeAuthenticationToken) authentication;
        String uniqueCode       = token.getPrincipal().toString();
        String shortCode        = token.getCredentials().toString();
        UserDetails userDetails = shortCodeDetailService.loadUserDetailByUniqueCode(uniqueCode);
        if(passwordEncoder.matches(shortCode,userDetails.getPassword())) {
            return mapToAuthenication(userDetails);
        }else{
            throw  new BadCredentialsException(messages.getMessage(
                    "ShortCodeAuthenticationProvider.badShortCode",
                    "shortCode is not current!"));
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication!=null&& ShortCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Authentication mapToAuthenication(UserDetails userDetails){
            ShortCodeAuthenticationToken authenticationToken = new ShortCodeAuthenticationToken(userDetails.getUsername(),userDetails.getAuthorities());
            authenticationToken.setAuthenticated(true);
            return authenticationToken;
    }

    public ShortCodeDetailService getShortCodeDetailService() {
        return shortCodeDetailService;
    }

    public void setShortCodeDetailService(ShortCodeDetailService shortCodeDetailService) {
        this.shortCodeDetailService = shortCodeDetailService;
    }

    public void setPasswordEncoder(PasswordEncoder encoder){
        this.passwordEncoder = encoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(passwordEncoder==null){
           logger.debug("passwordEndcoder is null ,use org.springframework.security.crypto.password.NoOpPasswordEncoder");
            passwordEncoder = NoOpPasswordEncoder.getInstance();
        }
        Assert.notNull(shortCodeDetailService,"shortCodeDetailService not be null!");
    }
}
