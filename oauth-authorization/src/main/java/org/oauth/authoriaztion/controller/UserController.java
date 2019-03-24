package org.oauth.authoriaztion.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserController {


    @RequestMapping(value = "/info",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object userInfo(){
        String userAtt ="aa";
        Map<String,Object> map = new HashMap<>(1);
        map.put(userAtt,SecurityContextHolder.getContext().getAuthentication());
        return map;
    }
}
