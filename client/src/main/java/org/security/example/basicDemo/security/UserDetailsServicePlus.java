package org.security.example.basicDemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Created by ttjkst on 2018/9/9.
 */
public class UserDetailsServicePlus implements UserDetailsService{


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String  password         = "admin";
        String  enCodingPassword = passwordEncoder.encode(password);
        UserDetails build = User.builder().password(enCodingPassword).username("admin").roles("USER").build();
        if(build.getUsername().equals(username)){
            return build;
        }else {
            throw new UsernameNotFoundException("no user");
        }
    }
}