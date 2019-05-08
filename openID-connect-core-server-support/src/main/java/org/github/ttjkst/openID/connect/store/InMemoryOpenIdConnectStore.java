package org.github.ttjkst.openID.connect.store;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOpenIdConnectStore implements OpenIdConnectStore{

    private static ConcurrentHashMap<String, UserDetails> storeMap = new ConcurrentHashMap<>(255);

    @Override
    public UserDetails loadAuthenticationBySubject(String subject) {
        return storeMap.get(subject);
    }

    @Override
    public void saveAuthenicationbySubject(String subject, UserDetails userDetail) {
        storeMap.put(subject,userDetail);
    }
}
