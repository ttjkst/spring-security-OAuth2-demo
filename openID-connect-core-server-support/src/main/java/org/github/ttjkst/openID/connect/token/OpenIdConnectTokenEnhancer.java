package org.github.ttjkst.openID.connect.token;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.*;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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

    private String verifierKey = (new RandomValueStringGenerator()).generate();

    private JsonParser objectMapper = JsonParserFactory.create();

    private Signer signer;

    private String signingKey;

    private SignatureVerifier verifier;

    public OpenIdConnectTokenEnhancer() {
        this.signer = new MacSigner(this.verifierKey);
        this.signingKey = this.verifierKey;
    }

    public String getVerifierKey() {
        return verifierKey;
    }

    public void setVerifierKey(String verifierKey) {
        this.verifierKey = verifierKey;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public void setKeyPair(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        Assert.state(privateKey instanceof RSAPrivateKey, "KeyPair must be an RSA ");
        this.signer = new RsaSigner((RSAPrivateKey)privateKey);
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        this.verifier = new RsaVerifier(publicKey);
        this.verifierKey = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.encode(publicKey.getEncoded())) + "\n-----END PUBLIC KEY-----";
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
            Map<String, Object> idTokenMap = new LinkedHashMap<>(10);
            idTokenMap.put(ISS,"https://www.example.com/openId-connect");
            idTokenMap.put(SUB,oAuth2AccessToken.getValue());
            idTokenMap.put(AUD,oAuth2Authentication.getOAuth2Request().getClientId());
            idTokenMap.put(EXP,oAuth2AccessToken.getExpiration().getTime());
            idTokenMap.put(IAT,System.currentTimeMillis());
            if(oAuth2Authentication.getOAuth2Request().getRequestParameters().containsKey("max_age")){
                idTokenMap.put(AUTH_TIME,System.currentTimeMillis());
            }
            String content = objectMapper.formatMap(idTokenMap);
            String idToken = JwtHelper.encode(content, this.signer).getEncoded();
            Map<String, Object> additionalInformation = new LinkedHashMap<>(enhanceAccessToken.getAdditionalInformation());
            additionalInformation.put(ID_TOKEN,idToken);

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
            JWTClaimsSet claimsSet = alice.build();
            SignedJWT signedJWT = new SignedJWT( new JWSHeader.Builder( RS256 )
                    .keyID( signer.algorithm())
                    .build(), claimsSet );
            additionalInformation.put(ID_TOKEN,signedJWT.serialize());
            enhanceAccessToken.setAdditionalInformation(additionalInformation);
            return enhanceAccessToken;
        }
        return oAuth2AccessToken;
    }

}
