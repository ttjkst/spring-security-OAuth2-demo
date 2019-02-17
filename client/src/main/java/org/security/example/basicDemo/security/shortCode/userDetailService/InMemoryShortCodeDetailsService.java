package org.security.example.basicDemo.security.shortCode.userDetailService;

import org.security.example.basicDemo.security.shortCode.ShortCodeDetailService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryShortCodeDetailsService implements ShortCodeDetailService {

    private static ConcurrentMap<String,UserDetails>  map = new ConcurrentHashMap(255);

    @Override
    public UserDetails loadUserDetailByUniqueCode(String uniqueCode){
        return map.get(uniqueCode);
    }


    public  void addUserDetails(String uniqueCode,UserDetails userDetails){
        map.put(uniqueCode,userDetails);
    }

    public void removeUserDetails(String uniqueCode){
        map.remove(uniqueCode);
    }
}
