package org.security.example.basicDemo.security.shortCode;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Collection;


/**
 * 自己实现一个权限认证，短码认证+唯一编码
 * */
public class ShortCodeAuthenticationToken extends AbstractAuthenticationToken {


    private String authCode;

    private String uniqueCode;

    private String userName;

    public ShortCodeAuthenticationToken(String code,String uniqueCode) {
        super(null);
        this.uniqueCode = uniqueCode;
        this.authCode = code;
    }

    public ShortCodeAuthenticationToken(String userName, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userName = userName;
        this.authCode = "N/A";
    }

    @Override
    public Object getCredentials() {
        return authCode;
    }

    @Override
    public Object getPrincipal() {
        return StringUtils.hasText(userName)?userName: uniqueCode;
    }
}
