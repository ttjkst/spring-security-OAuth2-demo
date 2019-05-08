package org.github.ttjkst.openID.connect.store;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * use for store openIdConnect Authentiaction
 * support the client load use info
 * */
public interface OpenIdConnectStore {

    UserDetails loadAuthenticationBySubject(String subject);

    void saveAuthenicationbySubject(String subject,UserDetails needSaveUserDetail);
}
