package org.security.example.basicDemo.security.shortCode;

import org.springframework.security.core.userdetails.UserDetails;

public interface ShortCodeDetailService {

    UserDetails loadUserDetailByUniqueCode(String uniqueCode);
}
