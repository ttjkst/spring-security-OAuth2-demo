package org.github.securityDemo.core.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class TokenStoreUseTokenEnhancer implements TokenEnhancer {

    private static Map<String, UserDetails> storeMap = new HashMap<>(255);


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        storeMap.put(accessToken.getValue(),(UserDetails) authentication.getUserAuthentication().getPrincipal());
        return accessToken;
    }

    public UserDetails getStoreAuthentication(String accessTokenValue){
            return storeMap.get(accessTokenValue);
    }
}
