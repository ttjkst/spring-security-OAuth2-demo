package org.github.ttjkst.openID.connect.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.security.KeyPair;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

public class OpenIdConnectTokenEnhancer implements TokenEnhancer {

    /**
     * openId,
     * required,this String must include in request
     * */
    private final static String OPEN_ID="openid";

    private final static String ID_TOKEN="id_token";

    /**
     * Issuer Identifier,
     * required ,
     * It is stand for the unqion Identity  of  authentication information.
     * */
    private final static String ISS ="iss";

    /**
     * Subject  Identifier,
     * required
     *
     * */
    private final static String SUB  ="sub";

    /**
     * Audience(s)
     * */
    private final static String AUD ="aud";


    /**
     *  Expiration time
     * */
    private final static String EXP  ="exp";

    /**
     *  Issued At Time
     * */
    private final static String IAT   ="iat";

    /**
     * AuthenticationTime
     * */
    private final static String AUTH_TIME  ="auth_time ";

    /**
     * nonce
     * */
    private final static String NONCE  ="nonce";


    /**
     * Authentication Methods References
     * */
    private final static String AMR   ="amr";


    /**
     * Authentication Context Class Reference
     * */
    private final static String ACR  ="acr";


    /**
     * Authorized party
     * */
    private final static String AZP   ="azp";



    private static Map<String,OAuth2AccessToken> tokenMap = new ConcurrentHashMap<>(255);

    private static AtomicInteger INTIAL_SUB = new AtomicInteger(1);





    private KeyPair keyPair;


    public void setKeyPair(KeyPair keyPair) {
        this.keyPair=keyPair;
    }

    private boolean containsOpenId(OAuth2AccessToken oAuth2AccessToken){
        Set<String> scopes = oAuth2AccessToken.getScope();
        if(scopes!=null){
            for (String scope : scopes) {
                if(OPEN_ID.equals(scope)){
                    return  true;
                }
            }
        }
        return false;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        if(containsOpenId(oAuth2AccessToken)){
            DefaultOAuth2AccessToken enhanceAccessToken = new DefaultOAuth2AccessToken(oAuth2AccessToken);
            Map<String, Object> additionalInformation = new LinkedHashMap<>( enhanceAccessToken.getAdditionalInformation());
            JWTClaimsSet.Builder alice = new JWTClaimsSet
                    .Builder()
                    .subject( oAuth2AccessToken.getValue() )
                    .issuer("https://www.example.com/openId-connect" )
                    .expirationTime( oAuth2AccessToken.getExpiration() )
                    .audience( oAuth2Authentication.getOAuth2Request().getClientId() )
                    .issueTime( new Date(  ));
            if(oAuth2Authentication.getOAuth2Request().getRequestParameters().containsKey("max_age")){
                alice.notBeforeTime( new Date(  ));
            }
            JWSSigner jwsSigner = new RSASSASigner(keyPair.getPrivate());
            JWTClaimsSet claimsSet = alice.build();
            SignedJWT signedJWT = new SignedJWT( new JWSHeader.Builder( RS256 )
                    .keyID( null)
                    .build(), claimsSet );
            try {
                signedJWT.sign( jwsSigner);
            } catch (JOSEException e) {
                throw new RuntimeException( "sign error",e);
            }
            additionalInformation.put(ID_TOKEN,signedJWT.serialize());
            enhanceAccessToken.setAdditionalInformation(additionalInformation);
            return enhanceAccessToken;
        }
        return oAuth2AccessToken;
    }

}
