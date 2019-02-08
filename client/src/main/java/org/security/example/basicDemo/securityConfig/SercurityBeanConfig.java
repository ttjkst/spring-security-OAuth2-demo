package org.security.example.basicDemo.securityConfig;

import org.security.example.basicDemo.security.shortCode.ShortCodeAuthenticationProvider;
import org.security.example.basicDemo.security.shortCode.ShortCodeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SercurityBeanConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShortCodeDetailService shortCodeDetailService;

    @Bean
    public  ShortCodeAuthenticationProvider  authenticationProvider(){
        ShortCodeAuthenticationProvider shortCodeAuthenticationProvider = new ShortCodeAuthenticationProvider();
        shortCodeAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        shortCodeAuthenticationProvider.setShortCodeDetailService(shortCodeDetailService);
        return shortCodeAuthenticationProvider;
    }

}
