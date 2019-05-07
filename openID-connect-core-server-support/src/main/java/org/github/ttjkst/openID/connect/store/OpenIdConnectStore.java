package org.github.ttjkst.openID.connect.store;


import org.springframework.security.core.Authentication;

/**
 * use for store openIdConnect Authentiaction
 * support the client load use info
 * */
public interface OpenIdConnectStore {

    Authentication loadAuthenticationBySubject(String subject);

    void saveAuthenicationbySubject(String subject,Authentication needSaveAuthentication);
}
